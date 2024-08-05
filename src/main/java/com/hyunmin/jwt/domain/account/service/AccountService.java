package com.hyunmin.jwt.domain.account.service;

import com.hyunmin.jwt.domain.account.dto.RegisterRequestDto;
import com.hyunmin.jwt.domain.account.dto.RegisterResponseDto;
import com.hyunmin.jwt.domain.account.entity.Member;
import com.hyunmin.jwt.domain.account.repository.MemberRepository;
import com.hyunmin.jwt.global.exception.RestException;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
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

    @Transactional
    public RegisterResponseDto register(RegisterRequestDto requestDto) {
        validateUsername(requestDto.username());
        String encodedPw = passwordEncoder.encode(requestDto.password());
        Member member = requestDto.toEntity(encodedPw);
        return RegisterResponseDto.from(memberRepository.save(member));
    }

    private void validateUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new RestException(ErrorCode.ACCOUNT_CONFLICT);
        }
    }
}
