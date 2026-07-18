package com.splitmate.service;

import com.splitmate.model.Expense;
import com.splitmate.model.Member;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BalanceService {

    // positive = should get money back, negative = owes
    public Map<Long, Double> calculateBalances(List<Member> members, List<Expense> expenses) {
        Map<Long, Double> balances = new HashMap<>();
        for (Member m : members) {
            balances.put(m.getId(), 0.0);
        }

        for (Expense expense : expenses) {
            Long payerId = expense.getPaidBy().getId();
            balances.put(payerId, balances.get(payerId) + expense.getAmount());

            // TODO 100 / 3 = 33.333333, should i round this somehow?
            double share = expense.getAmount() / expense.getParticipants().size();
            for (Member p : expense.getParticipants()) {
                balances.put(p.getId(), balances.get(p.getId()) - share);
            }
        }

//        for (Long id : balances.keySet()) {
//            System.out.println(id + " -> " + balances.get(id));
//        }
        return balances;
    }
}
