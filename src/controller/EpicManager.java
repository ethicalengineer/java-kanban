package controller;

import model.Epic;
import model.SubTask;

import java.util.ArrayList;
import java.util.HashMap;

public class EpicManager {
    private final HashMap<Long, Epic> epics = new HashMap<>();

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epicsList = new ArrayList<>();

        for (Long id : epics.keySet()) {
            epicsList.add(epics.get(id));
        }

        return epicsList;
    }

    public void removeAllEpics() {
        epics.clear();
    }

    public Epic getEpicById(long id) {
        return epics.get(id);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic) {
        addEpic(epic);
    }

    public void removeEpicById(long id) {
        epics.remove(id);
    }

    public ArrayList<SubTask> getAllSubTasksByEpicId(long id) {
        return getEpicById(id).getSubTasks();
    }
}
