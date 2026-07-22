package com.splitmate.dto;

import com.splitmate.model.Group;

import java.time.LocalDateTime;
import java.util.List;

public record GroupDto(Long id, String name, LocalDateTime createdAt, List<MemberDto> members) {

    public static GroupDto from(Group group) {
        List<MemberDto> members = group.getMembers().stream()
                .map(MemberDto::from)
                .toList();
        return new GroupDto(group.getId(), group.getName(), group.getCreatedAt(), members);
    }
}
