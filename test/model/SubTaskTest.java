package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @BeforeAll
    static void createTestEpic() {
        Epic testEpic = new Epic("Test Epic", "For subtask test purpose");
        testEpic.setId(1);
    }

    @Test
    void subTaskEqualsSubTaskIfTheirIdsAreEqual() {
        SubTask subTask1 = new SubTask("SomeTask", "SomeDescription", 1, Status.NEW);
        SubTask subTask2 = new SubTask("SomeTask", "SomeDescription", 1, Status.NEW);
        subTask1.setId(2);
        subTask2.setId(2);

        assertEquals(subTask1, subTask2);
    }
}