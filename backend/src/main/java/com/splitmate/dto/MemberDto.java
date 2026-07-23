package com.splitmate.dto;

import com.splitmate.model.Member;

public record MemberDto(Long id, String name, int avatar) {

    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getName(), member.getAvatar());
    }
}
