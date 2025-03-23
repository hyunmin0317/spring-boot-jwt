package com.hyunmin.jwt.domain.account.dto;

import com.hyunmin.jwt.global.common.entity.enums.MemberRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterResponse(
        Long id,
        String username,
        MemberRole role,
        LocalDateTime createdAt
) {

}
