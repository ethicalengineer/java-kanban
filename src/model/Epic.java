package model;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        changeEpicStatus();
    }

    public void updateSubTask(SubTask subTask) {
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).getId() == subTask.getId()) {
                subTasks.remove(i);
                subTasks.add(i, subTask);
                changeEpicStatus();
                return;
            }
        }
    }

    public void removeSubTask(long id) {
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).getId() == id) {
                subTasks.remove(i);
                changeEpicStatus();
                return;
            }
        }
    }

    public void removeAllSubTasks() {
        subTasks.clear();
        changeEpicStatus();
    }

    public void changeEpicStatus() {
        int doneSubTasksCounter = 0;
        int newSubTasksCounter = 0;

        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() == Status.IN_PROGRESS) {
                this.setStatus(Status.IN_PROGRESS);
                return;
            } else if (subTask.getStatus() == Status.DONE) {
                doneSubTasksCounter++;
            } else if (subTask.getStatus() == Status.NEW) {
                newSubTasksCounter++;
            }
        }

        if (doneSubTasksCounter == subTasks.size() && doneSubTasksCounter != 0) {
            this.setStatus(Status.DONE);
        } else if (newSubTasksCounter == subTasks.size() || subTasks.isEmpty()) {
            this.setStatus(Status.NEW);
        } else {
            this.setStatus(Status.IN_PROGRESS);
        }
    }
}
