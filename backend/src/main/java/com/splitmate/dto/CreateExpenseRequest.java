package com.splitmate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateExpenseRequest(
        @NotBlank(message = "description can't be empty")
        @Size(max = 100)
        String description,

        @Positive(message = "amount must be more than 0")
        long amountCents,

        @NotNull(message = "who paid?")
        Long paidById,

        @NotEmpty(message = "choose at least one person")
        List<Long> participantIds
) {
}
