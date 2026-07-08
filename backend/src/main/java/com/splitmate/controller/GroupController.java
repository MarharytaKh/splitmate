package com.splitmate.controller;

import com.splitmate.model.Expense;
import com.splitmate.model.Group;
import com.splitmate.model.Member;
import com.splitmate.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<Group> getGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable Long id) {
        return groupService.getGroup(id);
    }

    @PostMapping
    public Group createGroup(@RequestBody Map<String, String> body) {
        return groupService.createGroup(body.get("name"));
    }

    @PostMapping("/{id}/members")
    public Member addMember(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return groupService.addMember(id, body.get("name"));
    }

    @GetMapping("/{id}/expenses")
    public List<Expense> getExpenses(@PathVariable Long id) {
        return groupService.getExpenses(id);
    }

    @PostMapping("/{id}/expenses")
    public Expense addExpense(@PathVariable Long id, @RequestBody ExpenseRequest request) {
        return groupService.addExpense(id, request);
    }

    @DeleteMapping("/{id}/expenses/{expenseId}")
    public void deleteExpense(@PathVariable Long id, @PathVariable Long expenseId) {
        groupService.deleteExpense(expenseId);
    }
}
