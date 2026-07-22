package com.splitmate.dto;

public record TransferDto(MemberDto from, MemberDto to, long amountCents) {
}
