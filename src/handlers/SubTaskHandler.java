package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler  {
    public SubTaskHandler(TaskManager manager) {
        super(manager);
    }

    public void handle(HttpExchange h) throws IOException {
        String[] pathParts = h.getRequestURI().getPath().split("/");
        String method = h.getRequestMethod();

        if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
            switch (method) {
                case "GET": // GET /subtasks
                    readSubTasksHandler(h);
                    break;
                case "POST": // POST /subtasks
                    createSubTaskHandler(h);
                    break;
                default:
                    super.sendNotFound(h);
            }
        } else if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
            long subTaskId = Long.parseLong(pathParts[2]);
            switch (method) {
                case "GET": // GET /subtasks/{id}
                    readSubTaskByIdHandler(h, subTaskId);
                    break;
                case "PUT":
                case "POST": // POST /subtasks/{id}
                    updateSubTaskByIdHandler(h, subTaskId);
                    break;
                case "DELETE": // DELETE /subtasks/{id}
                    deleteSubTaskByIdHandler(h, subTaskId);
                    break;
                default:
                    super.sendNotFound(h);
            }
        } else {
            super.sendNotFound(h);
        }
    }

    private void readSubTasksHandler(HttpExchange h) throws IOException {
        super.sendText(h, 200, gson.toJson(manager.getAllSubTasks()));
    }

    private void createSubTaskHandler(HttpExchange h) throws IOException {
        try {
            String body = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            try {
                SubTask subTask = gson.fromJson(body, SubTask.class);
                long subTaskId = manager.addSubTask(subTask);
                if (subTaskId > 0) {
                    super.sendText(h, 201, "Подзадача с ID " + subTaskId + " успешно создана.");
                } else {
                    super.sendHasInteractions(h);
                }
            } catch (JsonSyntaxException e) {
                super.sendText(h, 422, "Переданное сообщение не является валидным JSON");
            }
        } catch (Exception e) {
            super.sendText(h, 500, e.getMessage());
        }
    }

    private void readSubTaskByIdHandler(HttpExchange h, long subTaskId) throws IOException {
        Optional<SubTask> subTask = manager.getSubTaskById(subTaskId);
        if (subTask.isPresent()) {
            super.sendText(h, 200, gson.toJson(subTask.get()));
        } else {
            super.sendNotFound(h);
        }
    }

    private void updateSubTaskByIdHandler(HttpExchange h, long subTaskId) throws IOException {
        Optional<SubTask> subTask = manager.getSubTaskById(subTaskId);
        if (subTask.isPresent()) {
            try {
                String body = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    SubTask updatedSubTask = gson.fromJson(body, SubTask.class);
                    updatedSubTask.setId(subTaskId);
                    manager.updateSubTask(updatedSubTask);
                    super.sendText(h, 201, "Подзадача с ID " + subTaskId + " успешно обновлена.");
                } catch (JsonSyntaxException e) {
                    super.sendText(h, 422, "Переданное сообщение не является валидным JSON");
                }
            } catch (Exception e) {
                super.sendText(h, 500, e.getMessage());
            }
        } else {
            super.sendNotFound(h);
        }
    }

    private void deleteSubTaskByIdHandler(HttpExchange h, long subTaskId) throws IOException {
        Optional<SubTask> subTask = manager.getSubTaskById(subTaskId);
        if (subTask.isPresent()) {
            manager.removeSubTaskById(subTaskId);
            super.sendText(h, 200, "Подзадача с ID " + subTaskId + " успешно удалена.");
        } else {
            super.sendNotFound(h);
        }
    }
}
