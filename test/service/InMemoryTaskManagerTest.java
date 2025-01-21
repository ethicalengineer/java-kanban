package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getInMemoryTaskManager();
    }

    @Test
    void createTaskTest() {
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        assertEquals(1, manager.getAllTasks().size());
    }

    @Test
    void getTaskByIdTest() {
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        assertEquals(1, manager.getTaskById(1).getId());
    }

    @Test
    void updateTaskByIdTest() {
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        Task updatedTask = manager.getTaskById(1);
        updatedTask.setStatus(Status.DONE);
        manager.updateTask(updatedTask);
        assertEquals(Status.DONE, manager.getTaskById(1).getStatus());
    }

    @Test
    void removeTaskByIdTest() {
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        manager.removeTaskById(1);
        assertEquals(0, manager.getAllTasks().size());
    }

    @Test
    void removeAllTasksTest() {
        manager.addTask(new Task("Полить цветы", "В гостиной", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        manager.addTask(new Task("Полить цветы", "На кухне", Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        manager.removeAllTasks();
        assertEquals(0, manager.getAllTasks().size());
    }

    @Test
    void createSubTaskTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Название", "Описание", 1, Status.IN_PROGRESS,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        assertEquals(1, manager.getAllSubTasks().size());
    }

    @Test
    void getSubTaskByIdTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Название", "Описание", 1, Status.IN_PROGRESS,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        assertEquals(2, manager.getSubTaskById(2).getId());
    }

    @Test
    void updateSubTaskByIdTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Название", "Описание", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        SubTask updatedSubTask = manager.getSubTaskById(2);
        updatedSubTask.setStatus(Status.DONE);
        manager.updateSubTask(updatedSubTask);
        assertEquals(Status.DONE, manager.getSubTaskById(2).getStatus());
    }

    @Test
    void getAllSubTaskByEpicIdTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Название", "Описание", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        manager.addSubTask(new SubTask("Название", "Описание", 1, Status.NEW,
                LocalDateTime.of(2140, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        assertEquals(2, manager.getAllSubTasksByEpicId(1).size());
    }

    @Test
    void removeSubTaskByIdTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Название", "Описание", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        manager.removeSubTaskById(2);
        assertEquals(0, manager.getAllSubTasks().size());
        assertEquals(0, manager.getEpicById(1).getSubTasks().size());
    }

    @Test
    void removeAllSubTasksTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Название", "Описание", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        manager.addSubTask(new SubTask("Название", "Описание", 1, Status.NEW,
                LocalDateTime.of(2140, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        manager.removeAllSubTasks();
        assertEquals(0, manager.getAllSubTasks().size());
        assertEquals(0, manager.getEpicById(1).getSubTasks().size());
    }

    @Test
    void createEpicTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        assertEquals(1, manager.getAllEpics().size());
    }

    @Test
    void getEpicByIdTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        assertEquals(1, manager.getEpicById(1).getId());
    }

    @Test
    void updateEpicByIdTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        Epic updatedEpic = manager.getEpicById(1);
        updatedEpic.setDescription("Измененное описание");
        manager.updateEpic(updatedEpic);
        assertEquals("Измененное описание", manager.getEpicById(1).getDescription());
    }

    @Test
    void removeEpicByIdTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.removeEpicById(manager.getEpicById(1).getId());
        assertEquals(0, manager.getAllEpics().size());
    }

    @Test
    void removeAllEpicsTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.removeAllEpics();
        assertEquals(0, manager.getAllEpics().size());
    }

    @Test
    void changeEpicStatusTest() {
        manager.addEpic(new Epic("Название", "Описание",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        assertEquals(Status.NEW, manager.getEpicById(1).getStatus());
        manager.addSubTask(new SubTask("Название", "Описание", 1, Status.DONE,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        assertEquals(Status.DONE, manager.getEpicById(1).getStatus());
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