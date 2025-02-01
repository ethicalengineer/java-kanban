package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class Epic extends Task {

    private final ArrayList<SubTask> subTasks;
    private LocalDateTime endTime;

    public Epic(String name, String description, LocalDateTime startTime) {
        super(name, description, Status.NEW, Type.EPIC, startTime, Duration.ZERO);
        subTasks = new ArrayList<>();
        endTime = startTime.plus(Duration.ZERO);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        changeEpicStatus();
        changeEpicEstimations();
    }

    public void updateSubTask(SubTask subTask) {
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).getId() == subTask.getId()) {
                subTasks.remove(i);
                subTasks.add(i, subTask);
                changeEpicStatus();
                changeEpicEstimations();
                return;
            }
        }
    }

    public void removeSubTask(long id) {
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).getId() == id) {
                subTasks.remove(i);
                changeEpicStatus();
                changeEpicEstimations();
                return;
            }
        }
    }

    public void removeAllSubTasks() {
        subTasks.clear();
        changeEpicStatus();
        changeEpicEstimations();
    }

    private void changeEpicStatus() {
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

    private void changeEpicEstimations() {
        super.setDuration(calculateDuration());

        if (!subTasks.isEmpty()) {
            setStartTime(calculateEpicStartTime());
            endTime = calculateEpicEndTime();
        }
    }

    private Duration calculateDuration() {
        return subTasks.stream().map(Task::getDuration).reduce(Duration.ZERO, Duration::plus);
    }

    private LocalDateTime calculateEpicStartTime() throws NoSuchElementException {
        return subTasks.stream().map(SubTask::getStartTime).min(Comparator.naturalOrder()).orElseThrow();
    }

    private LocalDateTime calculateEpicEndTime() throws NoSuchElementException {
        return subTasks.stream().map(SubTask::getEndTime).max(Comparator.naturalOrder()).orElseThrow();
    }

    // new toString() id, type, name, description, status, startTime, duration, endTime, epicId
    @Override
    public String toString() {
        return super.getId() + "," +
                Type.EPIC + "," +
                super.getName() + "," +
                super.getDescription() + "," +
                super.getStatus() + "," +
                super.getStartTime().format(DATE_TIME_FORMATTER) + "," +
                super.getDuration().toMinutes() + "," +
                endTime.format(DATE_TIME_FORMATTER) + "," +
                ",";
    }
}
