package model;

import exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicEqualsEpicIfTheirIdsAreEqual() {
        Epic epic1 = new Epic("SomeEpic", "SomeDescription",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0));
        Epic epic2 = new Epic("SomeEpic", "SomeDescription",
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0));
        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2);
    }

    @Test
    void epicRemoveAllSubTasksTest() {
        TaskManager manager = Managers.getInMemoryTaskManager();
        manager.addEpic(new Epic("Эпик 1", "Описание эпика 1",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        manager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 1, Status.IN_PROGRESS,
                LocalDateTime.of(2180, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        manager.addSubTask(new SubTask("Подзадача 3", "Описание подзадачи 3", 1, Status.DONE,
                LocalDateTime.of(2110, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));

        manager.getEpicById(1).orElseThrow(() -> new EntityNotFoundException("Epic", 1)).removeAllSubTasks();
        assertEquals(0, manager.getEpicById(1)
                .orElseThrow(() -> new EntityNotFoundException("Epic", 1))
                .getSubTasks()
                .size());
    }

    @Test
    void removeAllSubTasksOfRemovedEpicTest() {
        TaskManager manager = Managers.getInMemoryTaskManager();
        manager.addEpic(new Epic("Эпик 1", "Описание эпика 1",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        manager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 1, Status.IN_PROGRESS,
                LocalDateTime.of(2180, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        manager.addSubTask(new SubTask("Подзадача 3", "Описание подзадачи 3", 1, Status.DONE,
                LocalDateTime.of(2110, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));

        manager.removeEpicById(1);
        assertEquals(0, manager.getAllSubTasks().size());
    }
}