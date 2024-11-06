package controller;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> viewHistory = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        if (viewHistory.size() == 10) {
            viewHistory.removeFirst();
        }
        viewHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return viewHistory;
    }
}
