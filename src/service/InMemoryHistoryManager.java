package service;

import model.Task;
import model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    static class HistoryList {
        private Node<Task> head;
        private Node<Task> tail;
        private final Map<Long, Node<Task>> nodeHistory = new HashMap<>();

        public void linkLast(Task element) {
            if (nodeHistory.containsKey(element.getId())) {
                removeNode(element.getId());
            }

            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else
                oldTail.setNext(newNode);

            nodeHistory.put(element.getId(), newNode);
        }

        public void removeNode(long nodeId) {
            if (nodeHistory.containsKey(nodeId)) {
                Node<Task> deletedNode = nodeHistory.get(nodeId);
                final Node<Task> oldPrev = deletedNode.getPrev();
                final Node<Task> oldNext = deletedNode.getNext();

                if (oldPrev != null) {
                    if (oldNext != null) {
                        oldPrev.setNext(deletedNode.getNext());
                        oldNext.setPrev(deletedNode.getPrev());
                    } else {
                        oldPrev.setNext(null);
                        this.tail = oldPrev;
                    }
                } else {
                    if (oldNext != null) {
                        oldNext.setPrev(null);
                        this.head = oldNext;
                    } else {
                        this.head = null;
                        this.tail = null;
                    }
                }
                nodeHistory.remove(nodeId);
            }
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node<Task> currentNode = this.head;
            if (currentNode != null) {
                do {
                    tasks.add(currentNode.getData());
                    currentNode = currentNode.getNext();
                } while (currentNode != null);
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
