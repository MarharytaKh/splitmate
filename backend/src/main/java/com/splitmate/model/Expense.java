package com.splitmate.model;

import java.util.List;

public class Expense {

    private Long id;
    private String description;
    private double amount;
    private Member paidBy;
    private List<Member> participants;

    public Expense(Long id, String description, double amount, Member paidBy, List<Member> participants) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.participants = participants;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public Member getPaidBy() {
        return paidBy;
    }

    public List<Member> getParticipants() {
        return participants;
    }
}
