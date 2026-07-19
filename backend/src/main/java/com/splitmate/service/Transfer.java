package com.splitmate.service;

public record Transfer(Long fromId, Long toId, long amountCents) {
}
