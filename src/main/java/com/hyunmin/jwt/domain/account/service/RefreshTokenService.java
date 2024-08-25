package com.hyunmin.jwt.domain.account.service;

import com.hyunmin.jwt.domain.account.entity.RefreshToken;
import com.hyunmin.jwt.domain.account.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(Long memberId, String token) {
        RefreshToken refreshToken = RefreshToken.of(memberId, token);
        refreshTokenRepository.save(refreshToken);
    }
}
