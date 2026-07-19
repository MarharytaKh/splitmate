package com.splitmate.service;

import com.splitmate.model.Expense;
import com.splitmate.model.Member;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BalanceService {

    /**
     * Returns memberId -> balance in grosze.
     * Positive = should get money back, negative = owes money.
     */
    public Map<Long, Long> calculateBalances(List<Member> members, List<Expense> expenses) {
        Map<Long, Long> balances = new LinkedHashMap<>();
        for (Member m : members) {
            balances.put(m.getId(), 0L);
        }

        for (Expense expense : expenses) {
            Long payerId = expense.getPaidBy().getId();
            balances.merge(payerId, expense.getAmountCents(), Long::sum);

            long[] shares = splitEqually(expense.getAmountCents(), expense.getParticipants().size());
            for (int i = 0; i < shares.length; i++) {
                Long id = expense.getParticipants().get(i).getId();
                balances.merge(id, -shares[i], Long::sum);
            }
        }
        return balances;
    }

    /**
     * 1000 / 3 -> [334, 333, 333]
     * the leftover grosze go to the first people so the sum is always correct
     */
    public long[] splitEqually(long amount, int people) {
        if (people <= 0) {
            throw new IllegalArgumentException("people must be > 0");
        }
        long[] shares = new long[people];
        long base = amount / people;
        long rest = amount % people;
        for (int i = 0; i < people; i++) {
            shares[i] = base + (i < rest ? 1 : 0);
        }
        return shares;
    }
}
