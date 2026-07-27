package org.bnbalint.snackdaddy.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SnackRankTest {

    @Test
    void test_correctSize() {
        assert(SnackRank.values().length == 12);
    }

    @Test
    void test_containsRank1() {
        assertEquals("RANK_1", SnackRank.RANK_1.name());
    }

    @Test
    void test_containsRank2() {
        assertEquals("RANK_2", SnackRank.RANK_2.name());
    }

    @Test
    void test_containsRank3() {
        assertEquals("RANK_3", SnackRank.RANK_3.name());
    }

    @Test
    void test_containsRank4() {
        assertEquals("RANK_4", SnackRank.RANK_4.name());
    }

    @Test
    void test_containsRank5() {
        assertEquals("RANK_5", SnackRank.RANK_5.name());
    }

    @Test
    void test_containsRank6() {
        assertEquals("RANK_6", SnackRank.RANK_6.name());
    }

    @Test
    void test_containsRank7() {
        assertEquals("RANK_7", SnackRank.RANK_7.name());
    }

    @Test
    void test_containsRank8() {
        assertEquals("RANK_8", SnackRank.RANK_8.name());
    }

    @Test
    void test_containsRank9() {
        assertEquals("RANK_9", SnackRank.RANK_9.name());
    }

    @Test
    void test_containsRank10() {
        assertEquals("RANK_10", SnackRank.RANK_10.name());
    }

    @Test
    void test_containsUNRANKED() {
        assertEquals("UNRANKED", SnackRank.UNRANKED.name());
    }

    @Test
    void test_containsHAVE_NOT_TRIED() {
        assertEquals("HAVE_NOT_TRIED", SnackRank.HAVE_NOT_TRIED.name());
    }
}
