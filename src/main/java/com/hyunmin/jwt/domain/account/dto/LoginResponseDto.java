package com.hyunmin.jwt.domain.account.dto;

public record LoginResponseDto(
        Long memberId,
        String accessToken,
        String refreshToken
) {

}
