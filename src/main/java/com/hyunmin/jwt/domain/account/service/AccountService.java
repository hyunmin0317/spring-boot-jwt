package com.hyunmin.jwt.domain.account.service;

import com.hyunmin.jwt.domain.account.dto.LoginRequestDto;
import com.hyunmin.jwt.domain.account.dto.LoginResponseDto;
import com.hyunmin.jwt.domain.account.dto.RegisterRequestDto;
import com.hyunmin.jwt.domain.account.dto.RegisterResponseDto;
import com.hyunmin.jwt.domain.account.entity.Member;
import com.hyunmin.jwt.domain.account.repository.MemberRepository;
import com.hyunmin.jwt.global.exception.RestException;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
import com.hyunmin.jwt.global.security.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public RegisterResponseDto register(RegisterRequestDto requestDto) {
        validateUsername(requestDto.username());
        String encodedPw = passwordEncoder.encode(requestDto.password());
        Member member = requestDto.toEntity(encodedPw);
        return RegisterResponseDto.from(memberRepository.save(member));
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        Member member = memberRepository.findByUsername(requestDto.username())
                .orElseThrow(() -> new RestException(ErrorCode.ACCOUNT_NOT_FOUND));
        checkPassword(requestDto.password(), member.getPassword());
        String accessToken = tokenProvider.createAccessToken(member.getUsername(), member.getRole());
        return LoginResponseDto.of(member.getId(), accessToken);
    }

    private void validateUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new RestException(ErrorCode.ACCOUNT_CONFLICT);
        }
    }

    private void checkPassword(String requestPassword, String memberPassword) {
        if (!passwordEncoder.matches(requestPassword, memberPassword)) {
            throw new RestException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }
}
