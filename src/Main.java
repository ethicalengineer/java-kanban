import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) {

        final String TEST_FILE = "backend.txt";

        TaskManager manager = Managers.getDefault(TEST_FILE);
        manager.addTask(new Task("Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100)));
        manager.addEpic(new Epic("Эпик 1", "Описание эпика 1",
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0)));
        manager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 2, Status.NEW,
                LocalDateTime.of(2130, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        manager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 2, Status.IN_PROGRESS,
                LocalDateTime.of(2180, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));
        manager.addSubTask(new SubTask("Подзадача 3", "Описание подзадачи 3", 2, Status.DONE,
                LocalDateTime.of(2110, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15)));

        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(TEST_FILE);

        System.out.println(manager.getAllTasks().toString());
        System.out.println(manager.getAllEpics().toString());
        System.out.println(manager.getAllSubTasks().toString());

        System.out.println(loadedManager.getAllTasks().toString());
        System.out.println(loadedManager.getAllEpics().toString());
        System.out.println(loadedManager.getAllSubTasks().toString());

        System.out.println(loadedManager.getPrioritizedTasks());
    }
}