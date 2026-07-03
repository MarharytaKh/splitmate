package com.splitmate.model;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private Long id;
    private String name;
    private List<Member> members = new ArrayList<>(); // idk if list or set is better here
    private List<Expense> expenses = new ArrayList<>();

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}
