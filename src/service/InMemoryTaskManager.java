package service;

import exception.EntityNotFoundException;
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
    public Optional<Task> getTaskById(long id) {
        Optional<Task> task = Optional.ofNullable(tasks.get(id));
        task.ifPresent(history::add);
        return task;
    }

    @Override
    public long addTask(Task task) {
        if (isValidDataRange(task)) {
            final long id = getNewId();
            task.setId(id);
            tasks.put(id, task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
            return task.getId();
        } else {
            return -1;
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
        if (getTaskById(id).isPresent()) {
            prioritizedTasks.remove(getTaskById(id).get());
            history.remove(id);
            tasks.remove(id);
        }
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
    public Optional<SubTask> getSubTaskById(long id) {
        Optional<SubTask> subTask = Optional.ofNullable(subTasks.get(id));
        subTask.ifPresent(history::add);
        return subTask;
    }

    @Override
    public long addSubTask(SubTask subTask) {
        if (isValidDataRange(subTask)) {
            final long id = getNewId();
            subTask.setId(id);
            Optional<Epic> updatedEpic = getEpicById(subTask.getEpicId());
            if (updatedEpic.isPresent()) {
                updatedEpic.get().addSubTask(subTask);
                updateEpic(updatedEpic.get());
            }
            subTasks.put(id, subTask);
            if (subTask.getStartTime() != null) {
                prioritizedTasks.add(subTask);
            }
            return subTask.getId();
        } else {
            return -1;
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Optional<Epic> updatedEpic = getEpicById(subTask.getEpicId());
        if (updatedEpic.isPresent()) {
            updatedEpic.get().updateSubTask(subTask);
            updateEpic(updatedEpic.get());
        }
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
        Optional<SubTask> subTask = getSubTaskById(id);
        if (subTask.isPresent()) {
            Optional<Epic> updatedEpic = getEpicById(subTask.get().getEpicId());
            if (updatedEpic.isPresent()) {
                updatedEpic.get().removeSubTask(id);
                updateEpic(updatedEpic.get());
            }
            prioritizedTasks.remove(subTask.get());
            history.remove(id);
            subTasks.remove(id);
        }
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
    public Optional<Epic> getEpicById(long id) {
        Optional<Epic> epic = Optional.ofNullable(epics.get(id));
        epic.ifPresent(history::add);
        return epic;
    }

    @Override
    public long addEpic(Epic epic) {
        final long id = getNewId();
        epic.setId(id);
        epics.put(id, epic);
        return epic.getId();
    }

    @Override
    public void updateEpic(Epic epic) {
        getEpicById(epic.getId())
            .orElseThrow(() -> new EntityNotFoundException("Epic", id))
            .getSubTasks()
            .stream()
            .peek(epic::addSubTask)
            .close();
        epics.put(epic.getId(), epic);
    }

    @Override
    public void removeEpicById(long id) {
        Optional<Epic> removedEpic = getEpicById(id);
        if (removedEpic.isPresent()) {
            for (SubTask subTask : removedEpic.get().getSubTasks()) {
                prioritizedTasks.remove(subTask);
                history.remove(subTask.getId());
                subTasks.remove(subTask.getId());
            }
        }
        history.remove(id);
        epics.remove(id);
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksByEpicId(long id) {
        return getEpicById(id)
                .orElseThrow(() -> new EntityNotFoundException("Epic", id))
                .getSubTasks();
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
