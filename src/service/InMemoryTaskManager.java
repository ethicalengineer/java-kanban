package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private long id = 0;

    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, Epic> epics = new HashMap<>();
    private final HashMap<Long, SubTask> subTasks = new HashMap<>();

    private final HistoryManager history = Managers.getDefaultHistory();

    // Tasks methods

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public Task getTaskById(long id) {
        Task task = tasks.get(id);
        history.add(task);
        return task;
    }

    @Override
    public void addTask(Task task) {
        final long id = getNewId();
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void removeTaskById(long id) {
        tasks.remove(id);
    }

    // SubTasks methods

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.clear();

        for (Epic updatedEpic : epics.values()) {
            updatedEpic.removeAllSubTasks();
        }
    }

    @Override
    public SubTask getSubTaskById(long id) {
        SubTask subTask = subTasks.get(id);
        history.add(subTask);
        return subTask;
    }

    @Override
    public void addSubTask(SubTask subTask) {
        final long id = getNewId();
        subTask.setId(id);
        Epic updatedEpic = getEpicById(subTask.getEpicId());
        updatedEpic.addSubTask(subTask);
        updateEpic(updatedEpic);
        subTasks.put(id, subTask);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Epic updatedEpic = getEpicById(subTask.getEpicId());
        updatedEpic.updateSubTask(subTask);
        updateEpic(updatedEpic);
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public void removeSubTaskById(long id) {
        Epic updatedEpic = getEpicById(getSubTaskById(id).getEpicId());
        updatedEpic.removeSubTask(id);
        updateEpic(updatedEpic);
        subTasks.remove(id);
    }

    // Epics methods

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Epic getEpicById(long id) {
        Epic epic = epics.get(id);
        history.add(epic);
        return epic;
    }

    @Override
    public void addEpic(Epic epic) {
        final long id = getNewId();
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void removeEpicById(long id) {
        Epic removedEpic = getEpicById(id);
        for (SubTask subTask : removedEpic.getSubTasks()) {
            removeSubTaskById(subTask.getId());
        }

        epics.remove(id);
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksByEpicId(long id) {
        return getEpicById(id).getSubTasks();
    }

    // Util

    private long getNewId() {
        id++;
        return id;
    }
}
