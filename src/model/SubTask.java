package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final long epicId;

    public SubTask(String name,
                   String description,
                   long epicId,
                   Status status,
                   LocalDateTime startTime,
                   Duration duration) {
        super(name, description, status, Type.SUBTASK, startTime, duration);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    // new toString() id, type, name, description, status, startTime, duration, endTime, epicId
    @Override
    public String toString() {
        return super.getId() + "," +
                Type.SUBTASK + "," +
                super.getName() + "," +
                super.getDescription() + "," +
                super.getStatus() + "," +
                super.getStartTime().format(DATE_TIME_FORMATTER) + "," +
                super.getDuration().toMinutes() + "," +
                super.getEndTime().format(DATE_TIME_FORMATTER) + "," +
                epicId;
    }
}