package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @BeforeAll
    static void createTestEpic() {
        Epic testEpic = new Epic("Test Epic", "For subtask test purpose", LocalDateTime.of(2150, Month.DECEMBER, 6, 10, 0));
        testEpic.setId(1);
    }

    @Test
    void subTaskEqualsSubTaskIfTheirIdsAreEqual() {
        SubTask subTask1 = new SubTask("SomeTask", "SomeDescription", 1, Status.NEW,
                LocalDateTime.of(2160, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15));
        SubTask subTask2 = new SubTask("SomeTask", "SomeDescription", 1, Status.NEW,
                LocalDateTime.of(2170, Month.DECEMBER, 6, 10, 0), Duration.ofMinutes(15));
        subTask1.setId(2);
        subTask2.setId(2);

        assertEquals(subTask1, subTask2);
    }
}