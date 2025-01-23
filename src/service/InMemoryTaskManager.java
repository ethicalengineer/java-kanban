package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private long id = 0;

    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, Epic> epics = new HashMap<>();
    private final HashMap<Long, SubTask> subTasks = new HashMap<>();

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private final HistoryManager history = Managers.getDefaultHistory();

    // Tasks methods

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.values().stream().peek(prioritizedTasks::remove).close();
        tasks.keySet().stream().peek(history::remove).close();
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
        if (isValidDataRange(task)) {
            final long id = getNewId();
            task.setId(id);
            tasks.put(id, task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        } else {
            System.out.println("Пересечение оценки задачи " + task.getName());
        }
    }

    @Override
    public void updateTask(Task task) {
        prioritizedTasks.remove(tasks.get(task.getId()));
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        } else {
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public void removeTaskById(long id) {
        prioritizedTasks.remove(getTaskById(id));
        history.remove(id);
        tasks.remove(id);
    }

    // SubTasks methods

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.values().stream().peek(prioritizedTasks::remove).close();
        subTasks.keySet().stream().peek(history::remove).close();
        subTasks.clear();

        for (Epic epic : epics.values()) {
            epic.removeAllSubTasks();
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
        if (isValidDataRange(subTask)) {
            final long id = getNewId();
            subTask.setId(id);
            Epic updatedEpic = getEpicById(subTask.getEpicId());
            updatedEpic.addSubTask(subTask);
            updateEpic(updatedEpic);
            subTasks.put(id, subTask);
            if (subTask.getStartTime() != null) {
                prioritizedTasks.add(subTask);
            }
        } else {
            System.out.println("Пересечение оценки подзадачи " + subTask.getName());
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Epic updatedEpic = getEpicById(subTask.getEpicId());
        updatedEpic.updateSubTask(subTask);
        updateEpic(updatedEpic);
        prioritizedTasks.remove(subTasks.get(subTask.getId()));
        subTasks.put(subTask.getId(), subTask);
        if (subTask.getStartTime() != null) {
            prioritizedTasks.add(subTask);
        } else {
            prioritizedTasks.remove(subTask);
        }
    }

    @Override
    public void removeSubTaskById(long id) {
        Epic updatedEpic = getEpicById(getSubTaskById(id).getEpicId());
        updatedEpic.removeSubTask(id);
        updateEpic(updatedEpic);
        prioritizedTasks.remove(getSubTaskById(id));
        history.remove(id);
        subTasks.remove(id);
    }

    // Epics methods

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpics() {
        subTasks.values().stream().peek(prioritizedTasks::remove).close();
        epics.keySet().stream().peek(history::remove).close();
        epics.clear();
        removeAllSubTasks();
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
        removedEpic.getSubTasks().stream().peek(subTask -> {
            prioritizedTasks.remove(subTask);
            history.remove(subTask.getId());
            subTasks.remove(subTask.getId());
        }).close();

        history.remove(id);
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

    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    boolean isValidDataRange(Task task) {
        if (prioritizedTasks.isEmpty()) {
            return true;
        }
        for (Task priotirizedTask : prioritizedTasks) {
            if (task.getEndTime().isAfter(priotirizedTask.getStartTime())
                    && priotirizedTask.getEndTime().isAfter(task.getStartTime())) {
                return false;
            }
        }
        return true;
    }

    public HistoryManager getHistory() {
        return history;
    }
}
