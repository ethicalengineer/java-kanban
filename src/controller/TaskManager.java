package controller;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Long, Task> tasks = new HashMap<>();

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();

        for (Long id : tasks.keySet()) {
            tasksList.add(tasks.get(id));
        }

        return tasksList;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(long id) {
        return tasks.get(id);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        addTask(task);
    }

    public void removeTaskById(long id) {
        tasks.remove(id);
    }
}
