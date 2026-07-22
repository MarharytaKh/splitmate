package com.splitmate.service;

import com.splitmate.model.Expense;
import com.splitmate.model.Member;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BalanceServiceTest {

    private final BalanceService service = new BalanceService();

    private Member member(long id, String name) {
        Member m = new Member(name);
        m.setId(id);
        return m;
    }

    @Test
    void splitEquallyKeepsEveryGrosz() {
        long[] shares = service.splitEqually(1000, 3);
        assertArrayEquals(new long[]{334, 333, 333}, shares);

        long sum = 0;
        for (long s : shares) sum += s;
        assertEquals(1000, sum);
    }

    @Test
    void splitEquallyThrowsForZeroPeople() {
        assertThrows(IllegalArgumentException.class, () -> service.splitEqually(100, 0));
    }

    @Test
    void balancesAddUpToZero() {
        Member ola = member(1, "Ola");
        Member kuba = member(2, "Kuba");
        Member zosia = member(3, "Zosia");
        List<Member> all = List.of(ola, kuba, zosia);

        List<Expense> expenses = List.of(
                new Expense("pizza", 10000, ola, all),
                new Expense("taxi", 4500, kuba, List.of(kuba, zosia))
        );

        Map<Long, Long> balances = service.calculateBalances(all, expenses);

        assertEquals(10000 - 3334, balances.get(1L));
        assertEquals(4500 - 3333 - 2250, balances.get(2L));
        assertEquals(-3333 - 2250, balances.get(3L));
        assertEquals(0, balances.values().stream().mapToLong(Long::longValue).sum());
    }
}
