package com.hyunmin.jwt.domain.member.dto;

import com.hyunmin.jwt.global.common.entity.enums.MemberRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberInfoResponse(
        Long id,
        String username,
        MemberRole memberRole,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
