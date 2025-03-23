package com.hyunmin.jwt.domain.account.dto;

public record LoginResponse(
        Long memberId,
        String accessToken,
        String refreshToken
) {

}
