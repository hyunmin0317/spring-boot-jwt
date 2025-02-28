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

    public RegisterResponseDto register(RegisterRequestDto requestDto) {
        validateUsername(requestDto.username());
        String encodedPw = passwordEncoder.encode(requestDto.password());
        Member member = AccountMapper.INSTANCE.toEntity(requestDto, encodedPw);
        return AccountMapper.INSTANCE.toDto(memberRepository.save(member));
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        Member member = memberRepository.findByUsername(requestDto.username())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        checkPassword(requestDto.password(), member.getPassword());
        return generateToken(member.getId(), member.getRole());
    }

    public LoginResponseDto refresh(RefreshRequestDto requestDto) {
        jwtTokenProvider.validateToken(requestDto.refreshToken(), true);
        RefreshToken oldRefreshToken = refreshTokenService.findRefreshToken(requestDto.refreshToken());
        Member member = memberRepository.findById(oldRefreshToken.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        refreshTokenService.deleteRefreshToken(oldRefreshToken.getToken());
        return generateToken(member.getId(), member.getRole());
    }

    private LoginResponseDto generateToken(Long memberId, MemberRole memberRole) {
        String accessToken = jwtTokenProvider.createAccessToken(memberId, memberRole, false);
        String refreshToken = jwtTokenProvider.createAccessToken(memberId, memberRole, true);
        refreshTokenService.saveRefreshToken(memberId, refreshToken);
        return LoginResponseDto.of(memberId, accessToken, refreshToken);
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
