package com.splitmate.dto;

// balance > 0 means the group owes this person money, < 0 means they owe
public record BalanceDto(Long memberId, String name, int avatar, long balanceCents) {
}
