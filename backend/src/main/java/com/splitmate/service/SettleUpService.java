package com.splitmate.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Turns balances into a short list of "X pays Y" transfers.
 *
 * Greedy idea: always match the person who owes the most with the person
 * who should get the most. One of them is fully settled after every step,
 * so we get at most n-1 transfers. It is not always the absolute minimum
 * (that problem is NP-hard) but it is good enough for a group of friends.
 */
@Service
public class SettleUpService {

    public List<Transfer> settle(Map<Long, Long> balances) {
        // long[] = {memberId, amount}, biggest amount on top
        PriorityQueue<long[]> creditors = new PriorityQueue<>((a, b) -> Long.compare(b[1], a[1]));
        PriorityQueue<long[]> debtors = new PriorityQueue<>((a, b) -> Long.compare(b[1], a[1]));

        for (Map.Entry<Long, Long> entry : balances.entrySet()) {
            long balance = entry.getValue();
            if (balance > 0) {
                creditors.add(new long[]{entry.getKey(), balance});
            } else if (balance < 0) {
                debtors.add(new long[]{entry.getKey(), -balance});
            }
        }

        List<Transfer> result = new ArrayList<>();
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            long[] creditor = creditors.poll();
            long[] debtor = debtors.poll();

            long amount = Math.min(creditor[1], debtor[1]);
            result.add(new Transfer(debtor[0], creditor[0], amount));

            creditor[1] -= amount;
            debtor[1] -= amount;

            if (creditor[1] > 0) {
                creditors.add(creditor);
            }
            if (debtor[1] > 0) {
                debtors.add(debtor);
            }
        }
        return result;
    }
}
