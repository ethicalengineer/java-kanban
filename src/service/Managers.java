package service;

public class Managers {
    public static TaskManager getDefault(String fileName) {
        return new FileBackedTaskManager(fileName);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static InMemoryTaskManager getInMemoryTaskManager() { return new InMemoryTaskManager(); }
}
