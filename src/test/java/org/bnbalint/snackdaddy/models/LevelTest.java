package org.bnbalint.snackdaddy.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LevelTest {

    @Test
    void test_correctSize() {
        assert(Level.values().length == 3);
    }

    @Test
    void test_containsD5() {
        assertEquals("D5", Level.D5.name());
    }

    @Test
    void test_containsD4() {
        assertEquals("D4", Level.D4.name());
    }

    @Test
    void test_containsD3() {
        assertEquals("D3", Level.D3.name());
    }
}
