package com.splitmate.service;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SettleUpServiceTest {

    private final SettleUpService service = new SettleUpService();

    @Test
    void noTransfersWhenEveryoneIsEven() {
        Map<Long, Long> balances = Map.of(1L, 0L, 2L, 0L);
        assertTrue(service.settle(balances).isEmpty());
    }

    @Test
    void simpleCaseOnePersonPaidForTwo() {
        // 1 paid 30 zl for 1,2,3
        Map<Long, Long> balances = new LinkedHashMap<>();
        balances.put(1L, 2000L);
        balances.put(2L, -1000L);
        balances.put(3L, -1000L);

        List<Transfer> transfers = service.settle(balances);

        assertEquals(2, transfers.size());
        for (Transfer t : transfers) {
            assertEquals(1L, t.toId());
            assertEquals(1000L, t.amountCents());
        }
    }

    @Test
    void chainOfDebtsBecomesOneTransfer() {
        // A owes B 10, B owes C 10 -> A should just pay C
        Map<Long, Long> balances = new LinkedHashMap<>();
        balances.put(1L, -1000L);
        balances.put(2L, 0L);
        balances.put(3L, 1000L);

        List<Transfer> transfers = service.settle(balances);

        assertEquals(1, transfers.size());
        assertEquals(new Transfer(1L, 3L, 1000L), transfers.get(0));
    }

    @Test
    void afterTransfersEveryoneIsAtZero() {
        Map<Long, Long> balances = new LinkedHashMap<>();
        balances.put(1L, 4550L);
        balances.put(2L, -1234L);
        balances.put(3L, -2016L);
        balances.put(4L, 700L);
        balances.put(5L, -2000L);

        List<Transfer> transfers = service.settle(balances);

        Map<Long, Long> check = new HashMap<>(balances);
        for (Transfer t : transfers) {
            assertTrue(t.amountCents() > 0);
            check.merge(t.fromId(), t.amountCents(), Long::sum);
            check.merge(t.toId(), -t.amountCents(), Long::sum);
        }
        check.values().forEach(v -> assertEquals(0L, v));
        assertTrue(transfers.size() <= balances.size() - 1);
    }
}
