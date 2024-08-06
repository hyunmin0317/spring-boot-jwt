package com.hyunmin.jwt.domain.account.dto;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        String accessToken
) {

    public static LoginResponseDto of(String accessToken) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
