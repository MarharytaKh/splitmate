package com.splitmate.service;

import com.splitmate.dto.*;
import com.splitmate.exception.BadRequestException;
import com.splitmate.exception.NotFoundException;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<GroupDto> getAllGroups() {
        return groupRepository.findAll().stream().map(GroupDto::from).toList();
    }

    @Transactional(readOnly = true)
    public GroupDto getGroup(Long id) {
        return GroupDto.from(findGroup(id));
    }

    @Transactional
    public GroupDto createGroup(CreateGroupRequest request) {
        Group group = new Group(request.name().trim());
        return GroupDto.from(groupRepository.save(group));
    }

    @Transactional
    public MemberDto addMember(Long groupId, AddMemberRequest request) {
        Group group = findGroup(groupId);
        String name = request.name().trim();

        boolean taken = group.getMembers().stream().anyMatch(m -> m.getName().equalsIgnoreCase(name));
        if (taken) {
            throw new BadRequestException(name + " is already in this group");
        }

        Member member = new Member(name);
        group.addMember(member);
        return MemberDto.from(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public List<ExpenseDto> getExpenses(Long groupId) {
        return findGroup(groupId).getExpenses().stream().map(ExpenseDto::from).toList();
    }

    @Transactional
    public ExpenseDto addExpense(Long groupId, CreateExpenseRequest request) {
        Group group = findGroup(groupId);
        Map<Long, Member> membersById = group.getMembers().stream()
                .collect(Collectors.toMap(Member::getId, Function.identity()));

        Member paidBy = membersById.get(request.paidById());
        if (paidBy == null) {
            throw new BadRequestException("person who paid is not in this group");
        }

        List<Member> participants = new ArrayList<>();
        for (Long id : request.participantIds().stream().distinct().toList()) {
            Member m = membersById.get(id);
            if (m == null) {
                throw new BadRequestException("member " + id + " is not in this group");
            }
            participants.add(m);
        }

        Expense expense = new Expense(request.description().trim(), request.amountCents(), paidBy, participants);
        group.addExpense(expense);
        return ExpenseDto.from(expenseRepository.save(expense));
    }

    @Transactional
    public void deleteExpense(Long groupId, Long expenseId) {
        Group group = findGroup(groupId);
        Expense expense = expenseRepository.findById(expenseId)
                .filter(e -> e.getGroup().getId().equals(groupId))
                .orElseThrow(() -> new NotFoundException("expense " + expenseId + " not found"));
        group.getExpenses().remove(expense);
    }

    @Transactional(readOnly = true)
    public List<BalanceDto> getBalances(Long groupId) {
        Group group = findGroup(groupId);
        Map<Long, Long> balances = balanceService.calculateBalances(group.getMembers(), group.getExpenses());

        return group.getMembers().stream()
                .map(m -> new BalanceDto(m.getId(), m.getName(), balances.get(m.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransferDto> getSettlement(Long groupId) {
        Group group = findGroup(groupId);
        Map<Long, Long> balances = balanceService.calculateBalances(group.getMembers(), group.getExpenses());
        Map<Long, MemberDto> members = group.getMembers().stream()
                .collect(Collectors.toMap(Member::getId, MemberDto::from));

        return settleUpService.settle(balances).stream()
                .map(t -> new TransferDto(members.get(t.fromId()), members.get(t.toId()), t.amountCents()))
                .toList();
    }

    private Group findGroup(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("group " + id + " not found"));
    }
}
