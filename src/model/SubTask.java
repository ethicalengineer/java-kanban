package model;

public class SubTask extends Task {
    private final long epicId;

    public SubTask(String name, String description, long epicId, Status status) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }
}