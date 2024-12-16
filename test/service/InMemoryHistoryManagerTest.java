package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    private TaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getInMemoryTaskManager();
    }

    @Test
    void addTaskToHistoryTest() {
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW));
        HistoryManager history = manager.getHistory();
        history.add(manager.getTaskById(1));
        assertEquals(1, history.getHistory().size());
    }

    @Test
    void removeTaskFromHistoryTest() {
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW));
        HistoryManager history = manager.getHistory();
        history.add(manager.getTaskById(1));
        assertEquals(1, history.getHistory().size());
        history.remove(manager.getTaskById(1).getId());
        assertEquals(0, history.getHistory().size());
    }
}
