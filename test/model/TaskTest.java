package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void taskEqualsTaskIfTheirIdsAreEqual() {
        Task task1 = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        Task task2 = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2);
    }

    @Test
    void taskDescriptionTest() {
        Task task1 = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        task1.setDescription("AnotherDescription");
        assertEquals("AnotherDescription", task1.getDescription());
    }

    @Test
    void taskRenameTest() {
        Task task1 = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        task1.setName("NewName");
        assertEquals("NewName", task1.getName());
    }

    @Test
    void hashCodeTest() {
        Task task1 = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        task1.setId(1);
        Task task2 = new Task("SomeTask", "SomeDescription", Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(100));
        task2.setId(1);
        assertEquals(task1.hashCode(), task2.hashCode());
    }
}