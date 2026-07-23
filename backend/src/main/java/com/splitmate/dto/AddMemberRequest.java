package com.splitmate.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddMemberRequest(
        @NotBlank(message = "name can't be empty")
        @Size(max = 30, message = "name is too long (max 30)")
        String name,

        @Min(1) @Max(10)
        int avatar
) {
}
