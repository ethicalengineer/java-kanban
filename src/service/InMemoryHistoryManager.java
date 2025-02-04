package service;

import model.Task;
import model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    static class HistoryList {
        private final Node<Task> head = new Node<>(null, null, this.tail);
        private final Node<Task> tail = new Node<>(this.head, null, null);
        private final Map<Long, Node<Task>> nodeHistory = new HashMap<>();

        public void linkLast(Task element) {
            if (nodeHistory.containsKey(element.getId())) {
                removeNode(element.getId());
            }

            final Node<Task> newNode = new Node<>(this.tail.getPrev(), element, this.tail);
            this.tail.getPrev().setNext(newNode);
            this.tail.setPrev(newNode);

            nodeHistory.put(element.getId(), newNode);
        }

        public void removeNode(long nodeId) {
            if (!nodeHistory.containsKey(nodeId)) {
                return;
            }

            Node<Task> deletedNode = nodeHistory.get(nodeId);
            final Node<Task> oldPrev = deletedNode.getPrev();
            final Node<Task> oldNext = deletedNode.getNext();

            oldPrev.setNext(oldNext);
            oldNext.setPrev(oldPrev);

            nodeHistory.remove(nodeId);
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();

            Node<Task> currentNode = this.head.getNext();
            if (currentNode != null) {
                while (currentNode.getData() != null) {
                    tasks.add(currentNode.getData());
                    currentNode = currentNode.getNext();
                }
            }

            return tasks;
        }
    }

    private final HistoryList viewHistory = new HistoryList();

    @Override
    public void add(Task task) {
        viewHistory.linkLast(task);
    }

    @Override
    public void remove(long nodeId) {
        viewHistory.removeNode(nodeId);
    }

    @Override
    public List<Task> getHistory() {
        return viewHistory.getTasks();
    }
}
