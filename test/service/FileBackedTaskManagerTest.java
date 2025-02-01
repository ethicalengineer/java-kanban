package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>  {

    private final static String TEST_FILE = "testBackEnd.txt";

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(TEST_FILE);
    }

    private final TaskManager manager = createTaskManager();

    @AfterAll
    static void AfterAll() {
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}
