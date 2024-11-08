package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY_SIZE = 10;
    private final List<Task> viewHistory = new ArrayList<>(MAX_HISTORY_SIZE);

    @Override
    public void add(Task task) {
        if (task != null) {
            if (viewHistory.size() == MAX_HISTORY_SIZE) {
                viewHistory.removeFirst();
            }
            viewHistory.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return viewHistory;
    }
}
