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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final ExpenseRepository expenseRepository;
    private final BalanceService balanceService;
    private final SettleUpService settleUpService;

    public GroupService(GroupRepository groupRepository, MemberRepository memberRepository,
                        ExpenseRepository expenseRepository, BalanceService balanceService,
                        SettleUpService settleUpService) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.expenseRepository = expenseRepository;
        this.balanceService = balanceService;
        this.settleUpService = settleUpService;
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

        Expense expense = new Expense(request.description, request.amountCents, paidBy, participants);
        group.addExpense(expense);
        System.out.println("added expense " + request.description + " " + request.amountCents);
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

    @Transactional
    public Map<String, Long> getBalances(Long groupId) {
        Group group = getGroup(groupId);
        Map<Long, Long> balances = balanceService.calculateBalances(group.getMembers(), group.getExpenses());

        Map<String, Long> result = new LinkedHashMap<>();
        for (Member m : group.getMembers()) {
            result.put(m.getName(), balances.get(m.getId()));
        }
        return result;
    }

    // TODO return names instead of ids, frontend will need them
    @Transactional
    public List<Transfer> getSettlement(Long groupId) {
        Group group = getGroup(groupId);
        Map<Long, Long> balances = balanceService.calculateBalances(group.getMembers(), group.getExpenses());
        return settleUpService.settle(balances);
    }
}
