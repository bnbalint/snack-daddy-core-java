package org.bnbalint.snackdaddy.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RinkTest {

    @Test
    void test_correctSize() {
        assert(Rink.values().length == 2);
    }

    @Test
    void test_containsBAIREL() {
        assertEquals("BAIREL", Rink.BAIREL.name());
    }

    @Test
    void test_containsUPMC() {
        assertEquals("UPMC", Rink.UPMC.name());
    }
}
