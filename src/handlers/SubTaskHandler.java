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

        if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
            switch (h.getRequestMethod()) {
                case "GET": // GET /subtasks
                    super.sendText(h, 200, gson.toJson(manager.getAllSubTasks()));
                    break;
                case "POST": // POST /subtasks
                    String body = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    try {
                        SubTask subTask = gson.fromJson(body, SubTask.class);
                        long subTaskId = manager.addSubTask(subTask);
                        if (subTaskId > 0) {
                            super.sendText(h, 201, "Подзадача с ID " + subTaskId + " успешно создана.");
                        } else {
                            super.sendText(h, 406, "Невозможно добавить подзадачу. Пересечение по времени");
                        }
                    } catch (JsonSyntaxException e) {
                        super.sendText(h, 422, "Переданное сообщение не является валидным JSON");
                    }
                    break;
                default:
                    super.sendText(h, 404, "Метод не реализован");
            }
        } else if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
            long subTaskId = Long.parseLong(pathParts[2]);
            Optional<SubTask> subTask = manager.getSubTaskById(subTaskId);
            if (subTask.isPresent()) {
                switch (h.getRequestMethod()) {
                    case "GET": // GET /subtasks/{id}
                        super.sendText(h, 200, gson.toJson(subTask.get()));
                        break;
                    case "PUT":
                    case "POST": // POST /subtasks/{id}
                        String body = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        try {
                            SubTask updatedSubTask = gson.fromJson(body, SubTask.class);
                            updatedSubTask.setId(subTaskId);
                            manager.updateSubTask(updatedSubTask);
                            super.sendText(h, 201, "Подзадача с ID " + subTaskId + " успешно обновлена.");
                        } catch (JsonSyntaxException e) {
                            super.sendText(h, 422, "Переданное сообщение не является валидным JSON");
                        }
                        break;
                    case "DELETE": // DELETE /subtasks/{id}
                        manager.removeSubTaskById(subTaskId);
                        super.sendText(h, 200, "Подзадача с ID " + subTaskId + " успешно удалена.");
                        break;
                    default:
                        super.sendText(h, 404, "Метод не реализован");
                }
            } else {
                super.sendText(h, 404, "Подзадачи с ID " + pathParts[2] + " не существует");
            }
        } else {
            super.sendText(h, 404, "Метод не реализован");
        }
    }
}
