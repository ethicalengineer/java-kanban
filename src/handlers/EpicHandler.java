package handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    public void handle(HttpExchange h) throws IOException {
        String[] pathParts = h.getRequestURI().getPath().split("/");
        String method = h.getRequestMethod();

        if (pathParts.length == 2 && pathParts[1].equals("epics")) {
            switch (method) {
                case "GET": // GET /epics
                    readEpicsHandler(h);
                    break;
                case "POST": // POST /epics
                    createEpicHandler(h);
                    break;
                default:
                    super.sendNotFound(h);
            }
        } else if (pathParts.length == 3 && pathParts[1].equals("epics")) {
            long epicId = Long.parseLong(pathParts[2]);
            switch (method) {
                case "GET": // GET /epics/{id}
                    readEpicByIdHandler(h, epicId);
                    break;
                case "DELETE": // DELETE /epics/{id}
                    deleteEpicByIdHandler(h, epicId);
                    break;
                default:
                    super.sendNotFound(h);
            }
        } else if (pathParts.length == 4 && pathParts[1].equals("epics") && pathParts[3].equals("subtasks")) {
            long epicId = Long.parseLong(pathParts[2]);

            if (method.equals("GET")) { // GET /epics/{id}/subtasks
                readSubTasksByEpicIdHandler(h, epicId);
            } else {
                super.sendNotFound(h);
            }
        } else {
            super.sendNotFound(h);
        }
    }

    private void readEpicsHandler(HttpExchange h) throws IOException {
        super.sendText(h, 200, gson.toJson(manager.getAllEpics()));
    }

    private void createEpicHandler(HttpExchange h) throws IOException {
        try {
            String body = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            try {
                JsonElement jsonElement = JsonParser.parseString(body);
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                Epic epic = new Epic(jsonObject.get("name").getAsString(),
                        jsonObject.get("description").getAsString(),
                        LocalDateTime.parse(jsonObject.get("startTime").getAsString(), dateTimeFormatter));

                long epicId = manager.addEpic(epic);
                super.sendText(h, 201, "Эпик с ID " + epicId + " успешно создан.");
            } catch (NullPointerException | DateTimeParseException | JsonSyntaxException e) {
                super.sendText(h, 422, "Переданное сообщение не является валидным JSON");
            }
        } catch (Exception e) {
            super.sendText(h, 500, e.getMessage());
        }
    }

    private void readEpicByIdHandler(HttpExchange h, long epicId) throws IOException {
        Optional<Epic> epic = manager.getEpicById(epicId);
        if (epic.isPresent()) {
            super.sendText(h, 200, gson.toJson(epic.get()));
        } else {
            super.sendNotFound(h);
        }
    }

    private void deleteEpicByIdHandler(HttpExchange h, long epicId) throws IOException {
        Optional<Epic> epic = manager.getEpicById(epicId);
        if (epic.isPresent()) {
            manager.removeEpicById(epicId);
            super.sendText(h, 200, "Эпик с ID " + epicId + " успешно удален.");
        } else {
            super.sendNotFound(h);
        }
    }

    private void readSubTasksByEpicIdHandler(HttpExchange h, long epicId) throws IOException {
        Optional<Epic> epic = manager.getEpicById(epicId);
        if (epic.isPresent()) {
            super.sendText(h, 200, gson.toJson(epic.get().getSubTasks()));
        } else {
            super.sendNotFound(h);
        }
    }
}
