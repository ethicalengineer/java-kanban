package controller;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getAllTasks();

    void removeAllTasks();

    Task getTaskById(long id);

    void addTask(Task task);

    void updateTask(Task task);

    void removeTaskById(long id);

    ArrayList<SubTask> getAllSubTasks();

    void removeAllSubTasks();

    SubTask getSubTaskById(long id);

    void addSubTask(SubTask subTask);

    void updateSubTask(SubTask subTask);

    void removeSubTaskById(long id);

    ArrayList<Epic> getAllEpics();

    void removeAllEpics();

    Epic getEpicById(long id);

    void addEpic(Epic epic);

    void updateEpic(Epic epic);

    void removeEpicById(long id);

    ArrayList<SubTask> getAllSubTasksByEpicId(long id);
}
