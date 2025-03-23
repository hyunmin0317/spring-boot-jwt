package com.hyunmin.jwt.domain.account.service;

import com.hyunmin.jwt.domain.account.dto.*;
import com.hyunmin.jwt.domain.account.entity.RefreshToken;
import com.hyunmin.jwt.domain.account.mapper.AccountMapper;
import com.hyunmin.jwt.global.common.entity.Member;
import com.hyunmin.jwt.global.common.entity.enums.MemberRole;
import com.hyunmin.jwt.global.common.repository.MemberRepository;
import com.hyunmin.jwt.global.exception.GeneralException;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
import com.hyunmin.jwt.global.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public RegisterResponse register(RegisterRequest request) {
        validateUsername(request.username());
        String encodedPw = passwordEncoder.encode(request.password());
        Member member = AccountMapper.INSTANCE.toEntity(request, encodedPw);
        return AccountMapper.INSTANCE.toResponse(memberRepository.save(member));
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.username())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        checkPassword(request.password(), member.getPassword());
        return generateToken(member.getId(), member.getRole());
    }

    public LoginResponse refresh(RefreshRequest request) {
        jwtTokenProvider.validateToken(request.refreshToken(), true);
        RefreshToken oldRefreshToken = refreshTokenService.findRefreshToken(request.refreshToken());
        Member member = memberRepository.findById(oldRefreshToken.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        refreshTokenService.deleteRefreshToken(oldRefreshToken.getToken());
        return generateToken(member.getId(), member.getRole());
    }

    private LoginResponse generateToken(Long memberId, MemberRole memberRole) {
        String accessToken = jwtTokenProvider.createAccessToken(memberId, memberRole, false);
        String refreshToken = jwtTokenProvider.createAccessToken(memberId, memberRole, true);
        refreshTokenService.saveRefreshToken(memberId, refreshToken);
        return AccountMapper.INSTANCE.toResponse(memberId, accessToken, refreshToken);
    }

    private void validateUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new GeneralException(ErrorCode.ACCOUNT_CONFLICT);
        }
    }

    private void checkPassword(String requestPassword, String memberPassword) {
        if (!passwordEncoder.matches(requestPassword, memberPassword)) {
            throw new GeneralException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }
}
