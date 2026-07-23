package com.splitmate.controller;

import com.splitmate.dto.*;
import com.splitmate.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "http://localhost:5173") // react dev server, without this browser blocks requests
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<GroupDto> getGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{id}")
    public GroupDto getGroup(@PathVariable Long id) {
        return groupService.getGroup(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupDto createGroup(@Valid @RequestBody CreateGroupRequest request) {
        return groupService.createGroup(request);
    }

    @PostMapping("/{id}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberDto addMember(@PathVariable Long id, @Valid @RequestBody AddMemberRequest request) {
        return groupService.addMember(id, request);
    }

    @GetMapping("/{id}/expenses")
    public List<ExpenseDto> getExpenses(@PathVariable Long id) {
        return groupService.getExpenses(id);
    }

    @PostMapping("/{id}/expenses")
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseDto addExpense(@PathVariable Long id, @Valid @RequestBody CreateExpenseRequest request) {
        return groupService.addExpense(id, request);
    }

    @DeleteMapping("/{id}/expenses/{expenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long id, @PathVariable Long expenseId) {
        groupService.deleteExpense(id, expenseId);
    }

    @GetMapping("/{id}/balances")
    public List<BalanceDto> getBalances(@PathVariable Long id) {
        return groupService.getBalances(id);
    }

    @GetMapping("/{id}/settle-up")
    public List<TransferDto> settleUp(@PathVariable Long id) {
        return groupService.getSettlement(id);
    }
}
