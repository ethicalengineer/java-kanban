package service;

import exception.EntityNotFoundException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    private final TaskManager manager = createTaskManager();

    @Test
    void changeEpicStatusTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        assertEquals(Status.NEW, manager.getEpicById(1)
                .orElseThrow(() -> new EntityNotFoundException("Epic", 1))
                .getStatus());
        manager.addSubTask(new SubTask("Название", "Описание", 1, Status.DONE,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        assertEquals(Status.DONE, manager.getEpicById(1)
                .orElseThrow(() -> new EntityNotFoundException("Epic", 1))
                .getStatus());
    }

    @Test
    void startDateCollisionTest() {
        manager.addTask(new Task("Полить цветы", "В гостиной", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        manager.addTask(new Task("Полить цветы", "На кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        assertEquals(1, manager.getAllTasks().size());
    }
}