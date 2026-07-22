package com.splitmate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateGroupRequest(
        @NotBlank(message = "group name can't be empty")
        @Size(max = 50, message = "group name is too long (max 50)")
        String name
) {
}
