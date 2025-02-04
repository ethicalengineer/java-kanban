package handlers;

import adapters.EpicListTypeToken;
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

public class EpicHandlerTest {
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
    public void getEpicsTest() throws InterruptedException, IOException {
        Epic epic = new Epic("SomeEpic", "SomeDescription",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0));
        String epicJson = gson.toJson(epic);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            List<Epic> epicsFromResponse = gson.fromJson(response.body(), new EpicListTypeToken().getType());
            assertEquals(1, epicsFromResponse.size(), "Некорректное количество эпиков");
            assertEquals("SomeEpic", epicsFromResponse.getFirst().getName(), "Некорректное имя эпика");
        }
    }

    @Test
    public void getEpicTest() throws InterruptedException, IOException {
        Epic epic = new Epic("SomeEpic", "SomeDescription",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0));
        String epicJson = gson.toJson(epic);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create("http://localhost:8080/epics/1");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            Epic epicFromResponse = gson.fromJson(response.body(), Epic.class);
            assertEquals(manager.getEpicById(1).orElseThrow(
                    () -> new EntityNotFoundException("EPIC", 1)
            ).getName(), epicFromResponse.getName());
        }
    }

    @Test
    public void getMissingEpicTest() throws InterruptedException, IOException {
        Epic epic = new Epic("SomeEpic", "SomeDescription",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0));
        String epicJson = gson.toJson(epic);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create("http://localhost:8080/epics/2");
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
    public void createEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic("SomeEpic", "SomeDescription",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0));
        String epicJson = gson.toJson(epic);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(201, response.statusCode());

            List<Epic> epicsFromManager = manager.getAllEpics();
            assertNotNull(epicsFromManager, "Эпики не возвращаются");
            assertEquals(1, epicsFromManager.size(), "Некорректное количество эпиков");
            assertEquals("SomeEpic", epicsFromManager.getFirst().getName(), "Некорректное имя эпика");
        }
    }

    @Test
    public void deleteEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic("SomeEpic", "SomeDescription",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0));
        String epicJson = gson.toJson(epic);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(1, manager.getAllEpics().size(), "Некорректное количество эпиков");

            url = URI.create("http://localhost:8080/epics/1");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals(0, manager.getAllEpics().size(), "Не удалось удалить эпик");
        }
    }

    @Test
    public void getEpicSubtasksTest() throws IOException, InterruptedException {
        Epic epic = new Epic("SomeEpic", "SomeDescription",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0));
        String epicJson = gson.toJson(epic);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            SubTask subTask = new SubTask("SomeSubTask", "SomeDescription", 1, Status.NEW,
                    LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15));
            String subTaskJson = gson.toJson(subTask);

            url = URI.create("http://localhost:8080/subtasks");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create("http://localhost:8080/epics/1/subtasks");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<SubTask> subTasksFromResponse = gson.fromJson(response.body(), new SubTaskListTypeToken().getType());

            assertEquals(manager
                    .getEpicById(1)
                    .orElseThrow(
                        () -> new EntityNotFoundException("EPIC", 1)
                    )
                    .getSubTasks()
                    .getFirst()
                    .getName(), subTasksFromResponse.getFirst().getName(), "Не удалось получить подзадачи эпика");
        }
    }
}
