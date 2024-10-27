import controller.Manager;
import model.Task;
import model.Epic;
import model.SubTask;
import model.Status;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        manager.addTask(new Task("Полить цветы", "В гостиной и на кухне", Status.NEW));
        manager.addTask(new Task("Прочитать задачу", "Я.Практикум", Status.IN_PROGRESS));
        manager.addTask(new Task("Купить воду", "Из Я.Лавки Nestle", Status.NEW));
        manager.addTask(new Task("Заплатить за ЖКХ", "10 000 р", Status.NEW));
        manager.addTask(new Task("Выспаться", "Почти невозможно", Status.IN_PROGRESS));
        manager.addTask(new Task("Написать код", "Средней паршивости", Status.DONE));

        System.out.println("-".repeat(20));
        System.out.println(manager.getAllTasks());
        System.out.println("-".repeat(20));
        System.out.println(manager.getTaskById(1));

        Task updatedTask = manager.getTaskById(1);
        updatedTask.setStatus(Status.IN_PROGRESS);
        manager.updateTask(updatedTask);

        System.out.println("-".repeat(20));
        System.out.println(manager.getTaskById(1));

        manager.removeTaskById(1);

        System.out.println("-".repeat(20));
        System.out.println(manager.getAllTasks());

        manager.removeAllTasks();

        System.out.println("-".repeat(20));
        System.out.println(manager.getAllTasks());

        manager.addEpic(new Epic("Бизнес-план Гномов", "Как подзаработать"));
        manager.addEpic(new Epic("Купить в ВВ", "Список покупок"));

        System.out.println("-".repeat(20));
        System.out.println(manager.getAllEpics());

        manager.addSubTask(new SubTask("Украсть трусы", "План", 7, Status.NEW));
        manager.addSubTask(new SubTask("???", "Nobody Knows", 7, Status.NEW));
        manager.addSubTask(new SubTask("Получить прибыль", "План", 7, Status.NEW));
        manager.addSubTask(new SubTask("Молоко", "К покупке", 8, Status.DONE));

        System.out.println("-".repeat(20));
        System.out.println(manager.getAllSubTasks());

        System.out.println("-".repeat(20));
        System.out.println(manager.getEpicById(7));
        System.out.println(manager.getAllSubTasksByEpicId(7));

        System.out.println("-".repeat(20));
        System.out.println(manager.getEpicById(8));
        System.out.println(manager.getAllSubTasksByEpicId(8));

        manager.addSubTask(new SubTask("Кефир", "К покупке", 8, Status.NEW));

        System.out.println("-".repeat(20));
        System.out.println(manager.getEpicById(8));
        System.out.println(manager.getAllSubTasksByEpicId(8));

        manager.removeSubTaskById(12);
        manager.removeSubTaskById(13);

        System.out.println("-".repeat(20));
        System.out.println(manager.getEpicById(8));
        System.out.println(manager.getAllSubTasksByEpicId(8));

        // Тут заваливаемся
        manager.removeEpicById(8);

        System.out.println("-".repeat(20));
        System.out.println(manager.getAllEpics());

        SubTask updatedSubTask = manager.getSubTaskById(10);
        updatedSubTask.setName("Продать трусы");
        updatedSubTask.setDescription("Разобрались");
        updatedSubTask.setStatus(Status.DONE);
        manager.updateSubTask(updatedSubTask);

        System.out.println("-".repeat(20));
        System.out.println(manager.getEpicById(7));
        System.out.println(manager.getAllSubTasksByEpicId(7));

        updatedSubTask = manager.getSubTaskById(9);
        updatedSubTask.setStatus(Status.DONE);
        manager.updateSubTask(updatedSubTask);
        updatedSubTask = manager.getSubTaskById(11);
        updatedSubTask.setStatus(Status.DONE);
        manager.updateSubTask(updatedSubTask);

        System.out.println("-".repeat(20));
        System.out.println(manager.getEpicById(7));
        System.out.println(manager.getAllSubTasksByEpicId(7));

        manager.removeSubTaskById(10);

        System.out.println("-".repeat(20));
        System.out.println(manager.getEpicById(7));
        System.out.println(manager.getAllSubTasksByEpicId(7));

        manager.removeAllSubTasks();
        manager.getAllSubTasksByEpicId(7);

        System.out.println("-".repeat(20));
        System.out.println(manager.getEpicById(7));
        System.out.println(manager.getAllSubTasksByEpicId(7));

        manager.removeAllEpics();
        System.out.println("-".repeat(20));
        System.out.println(manager.getAllEpics());

    }
}
