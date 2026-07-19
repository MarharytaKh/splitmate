package com.splitmate.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BalanceServiceTest {

    private final BalanceService service = new BalanceService();

    @Test
    void splitEquallyKeepsEveryGrosz() {
        long[] shares = service.splitEqually(1000, 3);
        assertArrayEquals(new long[]{334, 333, 333}, shares);
    }

    @Test
    void splitEquallyThrowsForZeroPeople() {
        assertThrows(IllegalArgumentException.class, () -> service.splitEqually(100, 0));
    }
}
