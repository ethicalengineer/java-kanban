package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    private final static String TEST_FILE = "testBackEnd.txt";

    private TaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefault(TEST_FILE);
    }

    @AfterEach
    void afterEach() {
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createTaskTest() {
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        assertEquals("1,TASK,Полить цветы,В гостиной и на кухне,NEW,10:00 06.12.2150,100,11:40 06.12.2150,,", getEntitiesFromFile().getFirst());
    }

    @Test
    void updateTaskTest() {
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        Task updatedTask = manager.getTaskById(1);
        updatedTask.setStatus(Status.DONE);
        manager.updateTask(updatedTask);
        assertEquals("1,TASK,Полить цветы,В гостиной и на кухне,DONE,10:00 06.12.2150,100,11:40 06.12.2150,,", getEntitiesFromFile().getFirst());
    }

    @Test
    void removeTaskByIdTest() {
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        manager.addTask(new Task("Покормить кота", "Пушистый голоден", Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        assertEquals(2, getEntitiesFromFile().size());
        manager.removeTaskById(2);
        assertEquals(1, getEntitiesFromFile().size());
        assertEquals("1,TASK,Полить цветы,В гостиной и на кухне,NEW,10:00 06.12.2150,100,11:40 06.12.2150,,", getEntitiesFromFile().getFirst());
    }

    @Test
    void removeAllTasksTest() {
        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        manager.addTask(new Task("Покормить кота", "Пушистый голоден", Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        assertEquals(2, getEntitiesFromFile().size());
        manager.removeAllTasks();
        assertEquals(0, getEntitiesFromFile().size());
    }

    @Test
    void createEpicTest() {
        manager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        assertEquals("1,EPIC,Годовая цель,Выучить Java,NEW,10:00 06.12.2150,0,10:00 06.12.2150,,", getEntitiesFromFile().getFirst());
    }

    @Test
    void updateEpicTest() {
        manager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        Epic updatedEpic = manager.getEpicById(1);
        updatedEpic.setDescription("Выучить JavaScript");
        manager.updateEpic(updatedEpic);
        assertEquals("1,EPIC,Годовая цель,Выучить JavaScript,NEW,10:00 06.12.2150,0,10:00 06.12.2150,,", getEntitiesFromFile().getFirst());
    }

    @Test
    void removeEpicByIdTest() {
        manager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addEpic(new Epic("Годовая цель", "Выучить JavaScript",
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0)));
        assertEquals(2, getEntitiesFromFile().size());
        manager.removeEpicById(2);
        assertEquals(1, getEntitiesFromFile().size());
        assertEquals("1,EPIC,Годовая цель,Выучить Java,NEW,10:00 06.12.2150,0,10:00 06.12.2150,,", getEntitiesFromFile().getFirst());
    }

    @Test
    void removeAllEpicsTest() {
        manager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addEpic(new Epic("Годовая цель", "Выучить JavaScript",
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0)));
        assertEquals(2, getEntitiesFromFile().size());
        manager.removeAllEpics();
        assertEquals(0, getEntitiesFromFile().size());
    }

    @Test
    void createSubTaskTest() {
        manager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Научиться писать тесты", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        assertEquals("2,SUBTASK,Научиться писать тесты,Полностью,NEW,10:00 06.12.2130,15,10:15 06.12.2130,1", getEntitiesFromFile().getLast());
    }

    @Test
    void updateSubTaskTest() {
        manager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Научиться писать тесты", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        SubTask updatedSubTask = manager.getSubTaskById(2);
        updatedSubTask.setDescription("Немного");
        manager.updateSubTask(updatedSubTask);
        assertEquals("2,SUBTASK,Научиться писать тесты,Немного,NEW,10:00 06.12.2130,15,10:15 06.12.2130,1", getEntitiesFromFile().getLast());
    }

    @Test
    void removeSubTaskByIdTest() {
        manager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Научиться писать тесты", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        manager.addSubTask(new SubTask("Научиться исключениям", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2140, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        assertEquals(3, getEntitiesFromFile().size());
        manager.removeSubTaskById(3);
        assertEquals(2, getEntitiesFromFile().size());
        assertEquals("2,SUBTASK,Научиться писать тесты,Полностью,NEW,10:00 06.12.2130,15,10:15 06.12.2130,1", getEntitiesFromFile().getLast());
    }

    @Test
    void removeAllSubTasksTest() {
        manager.addEpic(new Epic("Годовая цель", "Выучить Java",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Научиться писать тесты", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        manager.addSubTask(new SubTask("Научиться исключениям", "Полностью", 1, Status.NEW,
                LocalDateTime.of(2140, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        assertEquals(3, getEntitiesFromFile().size());
        manager.removeAllSubTasks();
        assertEquals(1, getEntitiesFromFile().size());
    }

    @Test
    void loadFromFileTest() {
        manager.addTask(new Task("Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        manager.addEpic(new Epic("Эпик 1", "Описание эпика 1",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 2, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));

        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(TEST_FILE);

        assertEquals(manager.getAllTasks().size(), loadedManager.getAllTasks().size());
        assertEquals(manager.getAllEpics().size(), loadedManager.getAllEpics().size());
        assertEquals(manager.getAllSubTasks().size(), loadedManager.getAllSubTasks().size());
    }

    private List<String> getEntitiesFromFile() {
        boolean isFirstLine = true;
        List<String> taskLine = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(TEST_FILE))) {
            while (fileReader.ready()) {
                String buff = fileReader.readLine();
                if (isFirstLine) {
                    isFirstLine = false;
                } else {
                    if (buff != null) {
                        taskLine.add(buff);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }

        return taskLine;
    }

    @Test
    void getPrioritizedTasksTest() {
        manager.addTask(new Task("Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        manager.addEpic(new Epic("Эпик 1", "Описание эпика 1",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 2, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));

        assertTrue(manager.getPrioritizedTasks().getFirst().getStartTime()
                .isBefore(manager.getPrioritizedTasks().getLast().getStartTime()));
    }
}
