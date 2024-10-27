package controller;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private long id = 0;

    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, Epic> epics = new HashMap<>();
    private final HashMap<Long, SubTask> subTasks = new HashMap<>();

    // Tasks methods

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(long id) {
        return tasks.get(id);
    }

    public void addTask(Task task) {
        final long id = getNewId();
        task.setId(id);
        tasks.put(id, task);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void removeTaskById(long id) {
        tasks.remove(id);
    }

    // SubTasks methods

    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void removeAllSubTasks() {
        subTasks.clear();

        for (Epic updatedEpic : epics.values()) {
            updatedEpic.removeAllSubTasks();
        }
    }

    public SubTask getSubTaskById(long id) {
        return subTasks.get(id);
    }

    public void addSubTask(SubTask subTask) {
        final long id = getNewId();
        subTask.setId(id);
        Epic updatedEpic = getEpicById(subTask.getEpicId());
        updatedEpic.addSubTask(subTask);
        updateEpic(updatedEpic);
        subTasks.put(id, subTask);
    }

    public void updateSubTask(SubTask subTask) {
        Epic updatedEpic = getEpicById(subTask.getEpicId());
        updatedEpic.updateSubTask(subTask);
        updateEpic(updatedEpic);
        subTasks.put(subTask.getId(), subTask);
    }

    public void removeSubTaskById(long id) {
        Epic updatedEpic = getEpicById(getSubTaskById(id).getEpicId());
        updatedEpic.removeSubTask(id);
        updateEpic(updatedEpic);
        subTasks.remove(id);
    }

    // Epics methods

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public Epic getEpicById(long id) {
        return epics.get(id);
    }

    public void addEpic(Epic epic) {
        final long id = getNewId();
        epic.setId(id);
        epics.put(id, epic);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void removeEpicById(long id) {
        Epic removedEpic = getEpicById(id);
        for (SubTask subTask : removedEpic.getSubTasks()) {
            removeSubTaskById(subTask.getId());
        }

        epics.remove(id);
    }

    public ArrayList<SubTask> getAllSubTasksByEpicId(long id) {
        return getEpicById(id).getSubTasks();
    }

    private long getNewId() {
        id++;
        return id;
    }
}
