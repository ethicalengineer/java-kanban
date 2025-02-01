package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    private final static String TEST_FILE = "testBackEndManager.txt";

    @Test
    void getInMemoryTaskManager() {
        TaskManager manager = Managers.getInMemoryTaskManager();
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        assertEquals(1, manager.getTaskById(1).getId());
    }

    @Test
    void getInMemoryHistoryManager() {
        HistoryManager history = Managers.getDefaultHistory();
        Task task1 = new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        task1.setId(1);
        history.add(task1);
        assertEquals(1, history.getHistory().size());
    }

    @Test
    void getFileBackedTaskManager() {
        TaskManager manager = Managers.getDefault(TEST_FILE);
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        assertEquals("[1,TASK,Полить цветы,В гостиной и на кухне,NEW,10:00 06.12.2150,100,11:40 06.12.2150,,]", manager.getAllTasks().toString());
    }

    @AfterAll
    static void clearBackEnd() {
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}