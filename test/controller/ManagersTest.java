package controller;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getInMemoryTaskManager() {
        TaskManager manager = Managers.getDefault();
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
}