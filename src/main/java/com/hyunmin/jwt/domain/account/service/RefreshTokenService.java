package com.hyunmin.jwt.domain.account.service;

import com.hyunmin.jwt.domain.account.entity.RefreshToken;
import com.hyunmin.jwt.domain.account.repository.RefreshTokenRepository;
import com.hyunmin.jwt.global.exception.GeneralException;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
import com.hyunmin.jwt.global.security.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    public RefreshToken findRefreshToken(String token) {
        return refreshTokenRepository.findById(token)
                .orElseThrow(() -> new GeneralException(ErrorCode.INVALID_REFRESH_TOKEN));
    }

    @Transactional
    public void saveRefreshToken(Long memberId, String token) {
        long expirationTime = jwtProperties.getExpirationTime(true);
        RefreshToken refreshToken = RefreshToken.of(memberId, token, expirationTime);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        Optional<RefreshToken> target = refreshTokenRepository.findById(token);
        target.ifPresent(refreshTokenRepository::delete);
    }
}
