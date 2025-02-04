package handlers;

import com.google.gson.stream.MalformedJsonException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler  {
    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    public void handle(HttpExchange h) throws IOException {
        String[] pathParts = h.getRequestURI().getPath().split("/");

        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
            switch (h.getRequestMethod()) {
                case "GET": // GET /tasks
                    super.sendText(h, 200, gson.toJson(manager.getAllTasks()));
                    break;
                case "POST": // POST /tasks
                    String body = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    try {
                        Task task = gson.fromJson(body, Task.class);
                        long taskId = manager.addTask(task);
                        if (taskId > 0) {
                            super.sendText(h, 201, "Задача с ID " + taskId + " успешно создана.");
                        } else {
                            super.sendText(h, 406, "Невозможно добавить задачу. Пересечение по времени");
                        }
                    } catch (MalformedJsonException e) {
                        super.sendText(h, 422, "Переданное сообщение не является валидным JSON");
                    }
                    break;
                default:
                    super.sendText(h, 404, "Метод не реализован");
            }
        } else if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
            long taskId = Long.parseLong(pathParts[2]);
            Optional<Task> task = manager.getTaskById(taskId);
            if (task.isPresent()) {
                switch (h.getRequestMethod()) {
                    case "GET": // GET /tasks/{id}
                        super.sendText(h, 200, gson.toJson(task.get()));
                        break;
                    case "PUT":
                    case "POST": // POST /tasks/{id}
                        String body = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        try {
                            Task updatedTask = gson.fromJson(body, Task.class);
                            updatedTask.setId(taskId);
                            manager.updateTask(updatedTask);
                            super.sendText(h, 201, "Задача с ID " + taskId + " успешно обновлена.");
                        } catch (MalformedJsonException e) {
                            super.sendText(h, 422, "Переданное сообщение не является валидным JSON");
                        }
                        break;
                    case "DELETE": // DELETE /tasks/{id}
                        manager.removeTaskById(taskId);
                        super.sendText(h, 200, "Задача с ID " + taskId + " успешно удалена.");
                        break;
                    default:
                        super.sendText(h, 404, "Метод не реализован");
                }
            } else {
                super.sendText(h, 404, "Задачи с ID " + pathParts[2] + " не существует");
            }
        } else {
            super.sendText(h, 404, "Метод не реализован");
        }
    }
}
