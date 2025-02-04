package handlers;

import adapters.TaskListTypeToken;
import com.google.gson.Gson;
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

public class PrioritizedHandlerTest {
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
    public void prioritizedTasksTest() throws InterruptedException, IOException {
        Task centerTask = new Task("centerTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        Task lateTask = new Task("lateTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        Task earlyTask = new Task("earlyTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2140, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        manager.addTask(centerTask);
        manager.addTask(lateTask);
        manager.addTask(earlyTask);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/prioritized");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<Task> tasksFromResponse = gson.fromJson(response.body(), new TaskListTypeToken().getType());
            assertEquals(manager.getPrioritizedTasks().getFirst().getName(), tasksFromResponse.getFirst().getName(),
                    "Первый не на своем месте");
            assertEquals(manager.getPrioritizedTasks().getLast().getName(), tasksFromResponse.getLast().getName(),
                    "Последний не на своем месте");
        }
    }
}
