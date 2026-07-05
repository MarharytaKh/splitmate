package com.splitmate.controller;

import com.splitmate.model.Expense;
import com.splitmate.model.Group;
import com.splitmate.model.Member;
import com.splitmate.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    // TODO returns empty 200 when group doesnt exist, should be 404
    @GetMapping("/{id}")
    public Group getGroup(@PathVariable Long id) {
        return groupService.getGroup(id);
    }

    @PostMapping
    public Group createGroup(@RequestBody Map<String, String> body) {
        System.out.println("creating group: " + body);
        return groupService.createGroup(body.get("name"));
    }

    @PostMapping("/{id}/members")
    public Member addMember(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return groupService.addMember(id, body.get("name"));
    }

    @GetMapping("/{id}/expences")
    public List<Expense> getExpenses(@PathVariable Long id) {
        return groupService.getGroup(id).getExpenses();
    }

    @PostMapping("/{id}/expences")
    public Expense addExpense(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        System.out.println(body);
        String description = (String) body.get("description");
        double amount = ((Number) body.get("amount")).doubleValue();
        Long paidById = ((Number) body.get("paidById")).longValue();

        List<Long> participantIds = new ArrayList<>();
        for (Object o : (List<?>) body.get("participantIds")) {
            participantIds.add(((Number) o).longValue());
        }
        return groupService.addExpense(id, description, amount, paidById, participantIds);
    }
}
