package model;

public class SubTask extends Task {
    private final long epicId;

    public SubTask(String name, String description, long epicId, Status status) {
        super(name, description, status, Type.SUBTASK);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return super.getId() + "," + Type.SUBTASK + "," + super.getName() + "," + super.getStatus()
                + "," + super.getDescription() + "," + epicId;
    }
}