package handlers;

import adapters.SubTaskListTypeToken;
import com.google.gson.Gson;
import exception.EntityNotFoundException;
import model.Epic;
import model.Status;
import model.SubTask;
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

public class SubTaskHandlerTest {
    TaskManager manager = Managers.getInMemoryTaskManager();
    HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    Gson gson = httpTaskServer.getGson();

    @BeforeEach
    public void setUp() throws IOException {
        manager.removeAllTasks();
        manager.removeAllSubTasks();
        manager.removeAllEpics();
        Epic epic = new Epic("SomeEpic", "SomeDescription",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0));
        manager.addEpic(epic);
        httpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        httpTaskServer.stop();
    }

    @Test
    public void getSubTasksTest() throws InterruptedException, IOException {
        SubTask subTask = new SubTask("SomeSubTask", "SomeDescription", 1, Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15));
        String subTaskJson = gson.toJson(subTask);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            List<SubTask> subTasksFromResponse = gson.fromJson(response.body(), new SubTaskListTypeToken().getType());
            assertEquals(manager.getAllSubTasks().size(), subTasksFromResponse.size(), "Некорректное количество задач");
            assertEquals("SomeSubTask", subTasksFromResponse.getFirst().getName(), "Некорректное имя задачи");
        }
    }

    @Test
    public void getSubTaskTest() throws InterruptedException, IOException {
        SubTask subTask = new SubTask("SomeSubTask", "SomeDescription", 1, Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15));
        String subTaskJson = gson.toJson(subTask);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create("http://localhost:8080/subtasks/2");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            SubTask subTaskFromResponse = gson.fromJson(response.body(), SubTask.class);
            assertEquals(manager.getSubTaskById(2).orElseThrow(
                    () -> new EntityNotFoundException("SUBTASK", 2)
            ).getName(), subTaskFromResponse.getName());
        }
    }

    @Test
    public void getMissingSubTaskTest() throws InterruptedException, IOException {
        SubTask subTask = new SubTask("SomeSubTask", "SomeDescription", 1, Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15));
        String subTaskJson = gson.toJson(subTask);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create("http://localhost:8080/subtasks/3");
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
    public void createSubTaskTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("SomeSubTask", "SomeDescription", 1, Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15));
        String subTaskJson = gson.toJson(subTask);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(201, response.statusCode());

            List<SubTask> subTasksFormManager = manager.getAllSubTasks();
            assertNotNull(subTasksFormManager, "Подзадачи не возвращаются");
            assertEquals(1, subTasksFormManager.size(), "Некорректное количество подзадач");
            assertEquals("SomeSubTask", subTasksFormManager.getFirst().getName(), "Некорректное имя подзадачи");
        }
    }

    @Test
    public void createTaskWithInteractionTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("SomeSubTask", "SomeDescription", 1, Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15));
        String subTaskJson = gson.toJson(subTask);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(406, response.statusCode(), "Создание подзадачи с пересечением");
        }
    }

    @Test
    public void updateSubTaskTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("SomeSubTask", "SomeDescription", 1, Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15));
        String subTaskJson = gson.toJson(subTask);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(Status.NEW, manager.getSubTaskById(2).orElseThrow(
                    () -> new EntityNotFoundException("SUBTASK", 2)
            ).getStatus(), "Статусы не совпадают");

            SubTask updatedSubTask = new SubTask("SomeSubTask", "SomeDescription", 1, Status.IN_PROGRESS,
                    LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15));
            String updatedSubTaskJson = gson.toJson(updatedSubTask);

            url = URI.create("http://localhost:8080/subtasks/2");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(updatedSubTaskJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            assertEquals(Status.IN_PROGRESS, manager.getSubTaskById(2).orElseThrow(
                    () -> new EntityNotFoundException("SUBTASK", 2)
            ).getStatus(), "Статусы не совпадают");
        }
    }

    @Test
    public void deleteSubTaskTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("SomeSubTask", "SomeDescription", 1, Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15));
        String subTaskJson = gson.toJson(subTask);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(1, manager.getAllSubTasks().size(), "Некорректное количество подзадач");

            url = URI.create("http://localhost:8080/subtasks/2");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals(0, manager.getAllSubTasks().size(), "Не удалось удалить подзадачу");
        }
    }
}
