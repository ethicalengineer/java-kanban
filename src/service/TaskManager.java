package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TaskManager {
    ArrayList<Task> getAllTasks();

    void removeAllTasks();

    Optional<Task> getTaskById(long id);

    long addTask(Task task);

    void updateTask(Task task);

    void removeTaskById(long id);

    ArrayList<SubTask> getAllSubTasks();

    void removeAllSubTasks();

    Optional<SubTask> getSubTaskById(long id);

    long addSubTask(SubTask subTask);

    void updateSubTask(SubTask subTask);

    void removeSubTaskById(long id);

    ArrayList<Epic> getAllEpics();

    void removeAllEpics();

    Optional<Epic> getEpicById(long id);

    long addEpic(Epic epic);

    void updateEpic(Epic epic);

    void removeEpicById(long id);

    ArrayList<SubTask> getAllSubTasksByEpicId(long id);

    HistoryManager getHistory();

    List<Task> getPrioritizedTasks();
}
