package service;

import exception.EntityNotFoundException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void initializeManager() {
        taskManager = createTaskManager();
    }

    @Test
    public void testGetAllTasks() {
        taskManager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    public void testRemoveAllTasks() {
        taskManager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    public void testGetTaskById() {
        taskManager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        if (taskManager.getTaskById(1).isPresent()) {
            assertEquals("Полить цветы", taskManager.getTaskById(1).get().getName());
        }
    }

    @Test
    public void testAddTask() {
        taskManager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    public void testUpdateTask() {
        taskManager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        if (taskManager.getTaskById(1).isPresent()) {
            Task updatedTask = taskManager.getTaskById(1).get();
            updatedTask.setName("Покормить кота");
            taskManager.updateTask(updatedTask);
            assertEquals("Покормить кота", taskManager.getTaskById(1).get().getName());
        }
    }

    @Test
    public void testRemoveTaskById() {
        taskManager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        taskManager.removeTaskById(1);
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    public void testGetAllSubTasks() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        taskManager.addSubTask(new SubTask("Научиться писать тесты", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        taskManager.addSubTask(new SubTask("Научиться исключениям", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2140, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        assertEquals(2, taskManager.getAllSubTasks().size());
    }

    @Test
    public void testRemoveAllSubTasks() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        taskManager.addSubTask(new SubTask("Научиться писать тесты", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        taskManager.addSubTask(new SubTask("Научиться исключениям", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2140, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        taskManager.removeAllSubTasks();
        assertEquals(0, taskManager.getAllSubTasks().size());
    }

    @Test
    public void testGetSubTaskById() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        taskManager.addSubTask(new SubTask("Научиться писать тесты", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        taskManager.addSubTask(new SubTask("Научиться исключениям", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2140, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        Optional<SubTask> subTask = taskManager.getSubTaskById(3);
        subTask.ifPresent(task -> assertEquals("Научиться исключениям", task.getName()));
    }

    @Test
    public void testAddSubTask() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        taskManager.addSubTask(new SubTask("Научиться писать тесты", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        assertEquals(1, taskManager.getAllSubTasks().size());
    }

    @Test
    public void testUpdateSubTask() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        taskManager.addSubTask(new SubTask("Научиться писать тесты", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        taskManager.addSubTask(new SubTask("Научиться исключениям", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2140, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        Optional<SubTask> updatedSubTask = taskManager.getSubTaskById(2);
        if (updatedSubTask.isPresent()) {
            updatedSubTask.get().setName("Упасть с NPE");
            taskManager.updateSubTask(updatedSubTask.get());
            assertEquals("Упасть с NPE", updatedSubTask.get().getName());
        }
    }

    @Test
    public void testRemoveSubTaskById() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        taskManager.addSubTask(new SubTask("Научиться писать тесты", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        taskManager.addSubTask(new SubTask("Научиться исключениям", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2140, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        taskManager.removeSubTaskById(2);
        assertEquals(1, taskManager.getAllSubTasks().size());
    }

    @Test
    public void testGetAllEpics() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    public void testRemoveAllEpics() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getAllEpics().size());
    }

    @Test
    public void testGetEpicById() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        assertEquals("Годовая цель", taskManager.getEpicById(1)
                .orElseThrow(() -> new EntityNotFoundException("Epic", 1))
                .getName());
    }

    @Test
    public void testAddEpic() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    public void testUpdateEpic() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        Optional<Epic> updatedEpic = taskManager.getEpicById(1);
        updatedEpic
                .orElseThrow(() -> new EntityNotFoundException("Epic", 1))
                .setName("Промежуточная цель");
        taskManager.updateEpic(updatedEpic.get());
        assertEquals("Промежуточная цель", taskManager.getEpicById(1)
                .orElseThrow(() -> new EntityNotFoundException("Epic", 1))
                .getName());
    }

    @Test
    public void testRemoveEpicById() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        taskManager.removeEpicById(1);
        assertEquals(0, taskManager.getAllEpics().size());
    }

    @Test
    public void testGetAllSubTasksByEpicId() {
        taskManager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        taskManager.addSubTask(new SubTask("Научиться писать тесты", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        taskManager.addSubTask(new SubTask("Научиться исключениям", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2140, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        assertEquals(2, taskManager.getAllSubTasksByEpicId(1).size());
    }

    @Test
    public void testGetHistory() {
        taskManager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        HistoryManager history = taskManager.getHistory();
        taskManager.getTaskById(1);
        assertEquals(1, history.getHistory().size());
    }

    @Test
    public void testGetPrioritizedTasks() {
        taskManager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        taskManager.addTask(new Task("Покормить кота", "Шерстяной голоден", Status.NEW,
                LocalDateTime.of(2140, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        assertEquals("Покормить кота", taskManager.getPrioritizedTasks().getFirst().getName());
    }
}