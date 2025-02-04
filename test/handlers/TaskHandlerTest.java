package handlers;

import adapters.TaskListTypeToken;
import com.google.gson.Gson;
import exception.EntityNotFoundException;
import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HttpTaskServer;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskHandlerTest {
    TaskManager manager = Managers.getInMemoryTaskManager();
    HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    Gson gson = httpTaskServer.getGson();

    @BeforeEach
    public void setUp() throws IOException {
        manager.removeAllTasks();
        manager.removeAllSubTasks();
        manager.removeAllEpics();
        httpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        httpTaskServer.stop();
    }

    @Test
    public void getTasksTest() throws InterruptedException, IOException {
        Task task = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        String taskJson = gson.toJson(task);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            List<Task> tasksFromResponse = gson.fromJson(response.body(), new TaskListTypeToken().getType());
            assertEquals(1, tasksFromResponse.size(), "Некорректное количество задач");
            assertEquals("SomeTask", tasksFromResponse.getFirst().getName(), "Некорректное имя задачи");
        }
    }

    @Test
    public void getTaskTest() throws InterruptedException, IOException {
        Task task = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        String taskJson = gson.toJson(task);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create("http://localhost:8080/tasks/1");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            Task taskFromResponse = gson.fromJson(response.body(), Task.class);
            assertEquals(manager.getTaskById(1).orElseThrow(
                    () -> new EntityNotFoundException("TASK", 1)
            ).getName(), taskFromResponse.getName());
        }
    }

    @Test
    public void getMissingTaskTest() throws InterruptedException, IOException {
        Task task = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        String taskJson = gson.toJson(task);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create("http://localhost:8080/tasks/2");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
        }
    }

    @Test
    public void createTaskTest() throws IOException, InterruptedException {
        Task task = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        String taskJson = gson.toJson(task);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(201, response.statusCode());

            List<Task> tasksFromManager = manager.getAllTasks();
            assertNotNull(tasksFromManager, "Задачи не возвращаются");
            assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
            assertEquals("SomeTask", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
        }
    }

    @Test
    public void createTaskWithInteractionTest() throws IOException, InterruptedException {
        Task task = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        String taskJson = gson.toJson(task);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(406, response.statusCode(), "Создание задачи с пересечением");
        }
    }

    @Test
    public void updateTaskTest() throws IOException, InterruptedException {
        Task task = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        String taskJson = gson.toJson(task);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(Status.NEW, manager.getTaskById(1).orElseThrow(
                    () -> new EntityNotFoundException("TASK", 1)
            ).getStatus(), "Статусы не совпадают");

            Task updatedTask = new Task("SomeTask", "SomeDescription", Status.IN_PROGRESS,
                    LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));

            String updatedTaskJson = gson.toJson(updatedTask);

            url = URI.create("http://localhost:8080/tasks/1");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            assertEquals(Status.IN_PROGRESS, manager.getTaskById(1).orElseThrow(
                    () -> new EntityNotFoundException("TASK", 1)
            ).getStatus(), "Статусы не совпадают");
        }
    }

    @Test
    public void deleteTaskTest() throws IOException, InterruptedException {
        Task task = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        String taskJson = gson.toJson(task);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(1, manager.getAllTasks().size(), "Некорректное количество задач");

            url = URI.create("http://localhost:8080/tasks/1");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals(0, manager.getAllTasks().size(), "Не удалось удалить задачу");
        }
    }
}
