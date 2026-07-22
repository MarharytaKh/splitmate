package com.splitmate.dto;

import com.splitmate.model.Member;

public record MemberDto(Long id, String name) {

    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getName());
    }
}
