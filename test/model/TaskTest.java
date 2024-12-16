package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void taskEqualsTaskIfTheirIdsAreEqual() {
        Task task1 = new Task("SomeTask", "SomeDescription", Status.NEW);
        Task task2 = new Task("SomeTask", "SomeDescription", Status.NEW);
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2);
    }

    @Test
    void taskDescriptionTest() {
        Task task1 = new Task("SomeTask", "SomeDescription", Status.NEW);
        task1.setDescription("AnotherDescription");
        assertEquals("AnotherDescription", task1.getDescription());
    }

    @Test
    void taskRenameTest() {
        Task task1 = new Task("SomeTask", "SomeDescription", Status.NEW);
        task1.setName("NewName");
        assertEquals("NewName", task1.getName());
    }

    @Test
    void hashCodeTest() {
        Task task1 = new Task("SomeTask", "SomeDescription", Status.NEW);
        task1.setId(1);
        Task task2 = new Task("SomeTask", "SomeDescription", Status.NEW);
        task2.setId(1);
        assertEquals(task1.hashCode(), task2.hashCode());
    }
}