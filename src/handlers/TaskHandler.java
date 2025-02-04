package handlers;

import com.google.gson.JsonSyntaxException;
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
        String method = h.getRequestMethod();

        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
            switch (method) {
                case "GET": // GET /tasks
                    readTasksHandler(h);

                    break;
                case "POST": // POST /tasks
                    createTaskHandler(h);
                    break;
                default:
                    super.sendNotFound(h);
            }
        } else if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
            long taskId = Long.parseLong(pathParts[2]);
            switch (method) {
                case "GET": // GET /tasks/{id}
                    readTaskByIdHandler(h, taskId);
                    break;
                case "PUT":
                case "POST": // POST /tasks/{id}
                    updateTaskByIdHandler(h, taskId);
                    break;
                case "DELETE": // DELETE /tasks/{id}
                    deleteTaskByIdHandler(h, taskId);
                    break;
                default:
                    super.sendNotFound(h);
            }
        } else {
            super.sendNotFound(h);
        }
    }

    private void readTasksHandler(HttpExchange h) throws IOException {
        super.sendText(h, 200, gson.toJson(manager.getAllTasks()));
    }

    private void createTaskHandler(HttpExchange h) throws IOException {
        try {
            String body = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            try {
                Task task = gson.fromJson(body, Task.class);
                long taskId = manager.addTask(task);
                if (taskId > 0) {
                    super.sendText(h, 201, "Задача с ID " + taskId + " успешно создана.");
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

    private void readTaskByIdHandler(HttpExchange h, long taskId) throws IOException {
        Optional<Task> task = manager.getTaskById(taskId);
        if (task.isPresent()) {
            super.sendText(h, 200, gson.toJson(task.get()));
        } else {
            super.sendNotFound(h);
        }
    }

    private void updateTaskByIdHandler(HttpExchange h, long taskId) throws IOException {
        Optional<Task> task = manager.getTaskById(taskId);
        if (task.isPresent()) {
            try {
                String body = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    Task updatedTask = gson.fromJson(body, Task.class);
                    updatedTask.setId(taskId);
                    manager.updateTask(updatedTask);
                    super.sendText(h, 201, "Задача с ID " + taskId + " успешно обновлена.");
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

    private void deleteTaskByIdHandler(HttpExchange h, long taskId) throws IOException {
        Optional<Task> task = manager.getTaskById(taskId);
        if (task.isPresent()) {
            manager.removeTaskById(taskId);
            super.sendText(h, 200, "Задача с ID " + taskId + " успешно удалена.");
        } else {
            super.sendNotFound(h);
        }
    }
}
