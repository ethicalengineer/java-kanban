package controller;

import model.SubTask;
import model.Epic;

import java.util.ArrayList;
import java.util.HashMap;

public class SubTaskManager {
    private final HashMap<Long, SubTask> subTasks = new HashMap<>();
    EpicManager epicManager;

    public SubTaskManager(EpicManager epicManager) {
        this.epicManager = epicManager;
    }

    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> subTasksList = new ArrayList<>();

        for (Long id : subTasks.keySet()) {
            subTasksList.add(subTasks.get(id));
        }

        return subTasksList;
    }

    public void removeAllSubTasks() {
        subTasks.clear();

        for (Epic updatedEpic : epicManager.getAllEpics()) {
            updatedEpic.removeAllSubTasks();
        }
    }

    public SubTask getSubTaskById(long id) {
        return subTasks.get(id);
    }

    public void addSubTask(SubTask subTask) {
        Epic updatedEpic = epicManager.getEpicById(subTask.getEpicId());
        updatedEpic.addSubTask(subTask);
        epicManager.updateEpic(updatedEpic);
        subTasks.put(subTask.getId(), subTask);
    }

    public void updateSubTask(SubTask subTask) {
        Epic updatedEpic = epicManager.getEpicById(subTask.getEpicId());
        updatedEpic.updateSubTask(subTask);
        epicManager.updateEpic(updatedEpic);
        subTasks.put(subTask.getId(), subTask);
    }

    public void removeSubTaskById(long id) {
        Epic updatedEpic = epicManager.getEpicById(getSubTaskById(id).getEpicId());
        updatedEpic.removeSubTask(id);
        epicManager.updateEpic(updatedEpic);
        subTasks.remove(id);
    }
}
