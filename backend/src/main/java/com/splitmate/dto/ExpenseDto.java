package com.splitmate.dto;

import com.splitmate.model.Expense;
import com.splitmate.model.Member;

import java.time.LocalDateTime;
import java.util.List;

public record ExpenseDto(
        Long id,
        String description,
        long amountCents,
        Long paidById,
        String paidByName,
        List<Long> participantIds,
        LocalDateTime createdAt
) {

    public static ExpenseDto from(Expense e) {
        List<Long> ids = e.getParticipants().stream().map(Member::getId).toList();
        return new ExpenseDto(e.getId(), e.getDescription(), e.getAmountCents(),
                e.getPaidBy().getId(), e.getPaidBy().getName(), ids, e.getCreatedAt());
    }
}
