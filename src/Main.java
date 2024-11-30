import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.HistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();

        manager.addTask(new Task("Task1", "Task1 description", Status.NEW));
        manager.addTask(new Task("Task2", "Task2 description", Status.NEW));

        manager.addEpic(new Epic("Epic1", "Epic1 description"));
        manager.addSubTask(new SubTask("SubTask1", "SubTask1 description", 3, Status.NEW));
        manager.addSubTask(new SubTask("SubTask2", "SubTask2 description", 3, Status.NEW));
        manager.addSubTask(new SubTask("SubTask3", "SubTask3 description", 3, Status.NEW));

        manager.addEpic(new Epic("Epic2", "Epic2 description"));

        manager.getEpicById(3);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getSubTaskById(4);
        manager.getSubTaskById(5);

        HistoryManager history = manager.getHistory();
        System.out.println(history.getHistory());

        manager.getEpicById(3);
        System.out.println(history.getHistory());

        manager.removeTaskById(1);
        System.out.println(history.getHistory());

        manager.removeEpicById(3);
        System.out.println(history.getHistory());
    }
}