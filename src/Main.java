import controller.EpicManager;
import controller.SubTaskManager;
import controller.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

public class Main {
    private static long id = 0;

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        EpicManager epicManager = new EpicManager();
        SubTaskManager subTaskManager = new SubTaskManager(epicManager);

        taskManager.addTask(new Task(getNewId(), "Полить цветы", "В гостиной и на кухне", Status.NEW));
        taskManager.addTask(new Task(getNewId(), "Прочитать задачу", "Я.Практикум", Status.IN_PROGRESS));
        taskManager.addTask(new Task(getNewId(), "Купить воду", "Из Я.Лавки Nestle", Status.NEW));
        taskManager.addTask(new Task(getNewId(), "Заплатить за ЖКХ", "10 000 р", Status.NEW));
        taskManager.addTask(new Task(getNewId(), "Выспаться", "Почти невозможно", Status.IN_PROGRESS));
        taskManager.addTask(new Task(getNewId(), "Написать код", "Средней паршивости", Status.DONE));

        System.out.println("-".repeat(20));
        System.out.println(taskManager.getAllTasks());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getTaskById(1));

        taskManager.updateTask(new Task(1, "Полить все цветы", "Во всей квартире", Status.IN_PROGRESS));

        System.out.println("-".repeat(20));
        System.out.println(taskManager.getTaskById(1));

        taskManager.removeTaskById(1);

        System.out.println("-".repeat(20));
        System.out.println(taskManager.getAllTasks());

        taskManager.removeAllTasks();

        System.out.println("-".repeat(20));
        System.out.println(taskManager.getAllTasks());

        epicManager.addEpic(new Epic(getNewId(), "Бизнес-план Гномов", "Как подзаработать"));
        epicManager.addEpic(new Epic(getNewId(), "Купить в ВВ", "Список покупок"));

        System.out.println("-".repeat(20));
        System.out.println(epicManager.getAllEpics());

        subTaskManager.addSubTask(new SubTask(getNewId(), "Украсть трусы", "План", 7, Status.NEW));
        subTaskManager.addSubTask(new SubTask(getNewId(), "???", "Nobody Knows", 7, Status.NEW));
        subTaskManager.addSubTask(new SubTask(getNewId(), "Получить прибыль", "План", 7, Status.NEW));
        subTaskManager.addSubTask(new SubTask(getNewId(), "Молоко", "К покупке", 8, Status.DONE));

        System.out.println("-".repeat(20));
        System.out.println(subTaskManager.getAllSubTasks());

        System.out.println("-".repeat(20));
        System.out.println(epicManager.getEpicById(7));
        System.out.println(epicManager.getAllSubTasksByEpicId(7));

        System.out.println("-".repeat(20));
        System.out.println(epicManager.getEpicById(8));
        System.out.println(epicManager.getAllSubTasksByEpicId(8));

        subTaskManager.addSubTask(new SubTask(getNewId(), "Кефир", "К покупке", 8, Status.NEW));

        System.out.println("-".repeat(20));
        System.out.println(epicManager.getEpicById(8));
        System.out.println(epicManager.getAllSubTasksByEpicId(8));

        epicManager.getEpicById(8).removeAllSubTasks();

        System.out.println("-".repeat(20));
        System.out.println(epicManager.getEpicById(8));
        System.out.println(epicManager.getAllSubTasksByEpicId(8));

        epicManager.removeEpicById(8);

        System.out.println("-".repeat(20));
        System.out.println(epicManager.getAllEpics());

        subTaskManager.updateSubTask(new SubTask(
                10,
                "Продать трусы",
                "Разобрались",
                7, Status.DONE)
        );

        System.out.println("-".repeat(20));
        System.out.println(epicManager.getEpicById(7));
        System.out.println(epicManager.getAllSubTasksByEpicId(7));

        subTaskManager.updateSubTask(new SubTask(9, "Украсть трусы", "План", 7, Status.DONE));
        subTaskManager.updateSubTask(new SubTask(11, "Получить прибыль", "План", 7, Status.DONE));

        System.out.println("-".repeat(20));
        System.out.println(epicManager.getEpicById(7));
        System.out.println(epicManager.getAllSubTasksByEpicId(7));

        subTaskManager.removeSubTaskById(10);

        System.out.println("-".repeat(20));
        System.out.println(epicManager.getEpicById(7));
        System.out.println(epicManager.getAllSubTasksByEpicId(7));

        subTaskManager.removeAllSubTasks();
        epicManager.getAllSubTasksByEpicId(7);

        System.out.println("-".repeat(20));
        System.out.println(epicManager.getEpicById(7));
        System.out.println(epicManager.getAllSubTasksByEpicId(7));

        epicManager.removeAllEpics();
        System.out.println("-".repeat(20));
        System.out.println(epicManager.getAllEpics());

    }

    // Генератор ID
    private static long getNewId() {
        id++;
        return id;
    }
}
