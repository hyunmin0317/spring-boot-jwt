package com.hyunmin.jwt.domain.account.dto;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        Long memberId,
        String accessToken
) {

    public static LoginResponseDto of(Long memberId, String accessToken) {
        return LoginResponseDto.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .build();
    }
}
