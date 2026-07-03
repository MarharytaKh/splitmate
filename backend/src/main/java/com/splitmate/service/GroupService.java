package com.splitmate.service;

import com.splitmate.model.Expense;
import com.splitmate.model.Group;
import com.splitmate.model.Member;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO move this to a real database later, for now everything is in memory
@Service
public class GroupService {

    private Map<Long, Group> groups = new HashMap<>();
    private long nextId = 1;

    public Group createGroup(String name) {
        Group group = new Group(nextId++, name);
        groups.put(group.getId(), group);
        return group;
    }

    public List<Group> getAllGroups() {
        return new ArrayList<>(groups.values());
    }

    public Group getGroup(Long id) {
        return groups.get(id);
    }

    public Member addMember(Long groupId, String name) {
        Group group = groups.get(groupId);
        Member member = new Member(nextId++, name);
        group.getMembers().add(member);
        return member;
    }

    public Expense addExpense(Long groupId, String description, double amount, Long paidById, List<Long> participantIds) {
        Group group = groups.get(groupId);

        Member paidBy = null;
        List<Member> participants = new ArrayList<>();
        for (Member m : group.getMembers()) {
            if (m.getId().equals(paidById)) {
                paidBy = m;
            }
            if (participantIds.contains(m.getId())) {
                participants.add(m);
            }
        }

        Expense expense = new Expense(nextId++, description, amount, paidBy, participants);
        group.getExpenses().add(expense);
        return expense;
    }
}
