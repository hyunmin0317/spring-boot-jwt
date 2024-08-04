package com.hyunmin.jwt.domain.account.dto;

import com.hyunmin.jwt.domain.account.entity.Member;
import com.hyunmin.jwt.domain.account.entity.enums.MemberRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterResponseDto(
        Long id,
        String username,
        MemberRole role,
        LocalDateTime createdAt
) {

    public static RegisterResponseDto from(Member member) {
        return RegisterResponseDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
