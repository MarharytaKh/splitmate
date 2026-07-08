package com.splitmate.service;

import com.splitmate.controller.ExpenseRequest;
import com.splitmate.model.Expense;
import com.splitmate.model.Group;
import com.splitmate.model.Member;
import com.splitmate.repository.ExpenseRepository;
import com.splitmate.repository.GroupRepository;
import com.splitmate.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    // old version before db:
    // private Map<Long, Group> groups = new HashMap<>();
    // private long nextId = 1;

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final ExpenseRepository expenseRepository;

    public GroupService(GroupRepository groupRepository, MemberRepository memberRepository,
                        ExpenseRepository expenseRepository) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.expenseRepository = expenseRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroup(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    public Group createGroup(String name) {
        return groupRepository.save(new Group(name));
    }

    @Transactional
    public Member addMember(Long groupId, String name) {
        Group group = getGroup(groupId);
        if (group == null) {
            return null;
        }
        Member member = new Member(name);
        group.addMember(member);
        return memberRepository.save(member);
    }

    @Transactional
    public Expense addExpense(Long groupId, ExpenseRequest request) {
        Group group = getGroup(groupId);
        if (group == null) {
            return null;
        }

        Member paidBy = memberRepository.findById(request.paidById).orElse(null);
        List<Member> participants = new ArrayList<>();
        for (Long id : request.participantIds) {
            memberRepository.findById(id).ifPresent(participants::add);
        }

        Expense expense = new Expense(request.description, request.amount, paidBy, participants);
        group.addExpense(expense);
        System.out.println("added expense " + request.description + " " + request.amount);
        return expenseRepository.save(expense);
    }

    @Transactional
    public List<Expense> getExpenses(Long groupId) {
        Group group = getGroup(groupId);
        if (group == null) {
            return new ArrayList<>();
        }
        group.getExpenses().forEach(e -> e.getParticipants().size()); // load lazy list
        return group.getExpenses();
    }

    @Transactional
    public void deleteExpense(Long expenseId) {
        expenseRepository.deleteById(expenseId);
    }
}
