package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicEqualsEpicIfTheirIdsAreEqual() {
        Epic epic1 = new Epic("SomeEpic", "SomeDescription");
        Epic epic2 = new Epic("SomeEpic", "SomeDescription");
        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2);
    }
}