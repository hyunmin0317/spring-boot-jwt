package com.hyunmin.jwt.domain.account.service;

import com.hyunmin.jwt.domain.account.dto.RegisterRequestDto;
import com.hyunmin.jwt.domain.account.dto.RegisterResponseDto;
import com.hyunmin.jwt.domain.account.entity.Member;
import com.hyunmin.jwt.domain.account.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponseDto register(RegisterRequestDto requestDto) {
        String encodedPw = passwordEncoder.encode(requestDto.password());
        Member member = requestDto.toEntity(encodedPw);
        return RegisterResponseDto.from(memberRepository.save(member));
    }
}
