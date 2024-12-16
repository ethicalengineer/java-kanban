import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.*;

public class Main {
    public static void main(String[] args) {

        final String TEST_FILE = "backend.txt";

        TaskManager manager = Managers.getDefault(TEST_FILE);
        manager.addTask(new Task("Задача 1", "Описание задачи 1", Status.NEW));
        manager.addEpic(new Epic("Эпик 1", "Описание эпика 1"));
        manager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 2, Status.NEW));

        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(TEST_FILE);

        System.out.println(manager.getAllTasks().toString());
        System.out.println(manager.getAllEpics().toString());
        System.out.println(manager.getAllSubTasks().toString());

        System.out.println(loadedManager.getAllTasks().toString());
        System.out.println(loadedManager.getAllEpics().toString());
        System.out.println(loadedManager.getAllSubTasks().toString());
    }
}