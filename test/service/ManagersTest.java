package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    private final static String TEST_FILE = "testBackEndManager.txt";

    @Test
    void getInMemoryTaskManager() {
        TaskManager manager = Managers.getInMemoryTaskManager();
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW));
        assertEquals(1, manager.getTaskById(1).getId());
    }

    @Test
    void getInMemoryHistoryManager() {
        HistoryManager history = Managers.getDefaultHistory();
        Task task1 = new Task("Полить цветы", "В гостиной и на кухне", Status.NEW);
        task1.setId(1);
        history.add(task1);
        assertEquals(1, history.getHistory().size());
    }

    @Test
    void getFileBackedTaskManager() {
        TaskManager manager = Managers.getDefault(TEST_FILE);
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW));
        assertEquals("[1,TASK,Полить цветы,NEW,В гостиной и на кухне]", manager.getAllTasks().toString());
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