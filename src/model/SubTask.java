package model;

public class SubTask extends Task {
    private final long epicId;

    public SubTask(long id, String name, String description, long epicId, Status status) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }
}