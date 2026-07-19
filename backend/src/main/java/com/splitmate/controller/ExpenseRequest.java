package com.splitmate.controller;

import java.util.List;

public class ExpenseRequest {
    public String description;
    public long amountCents;
    public Long paidById;
    public List<Long> participantIds;
}
