package com.hyunmin.jwt.domain.member.service;

import com.hyunmin.jwt.domain.member.dto.ChangePasswordRequestDto;
import com.hyunmin.jwt.domain.member.dto.MemberInfoResponseDto;
import com.hyunmin.jwt.global.common.entity.Member;
import com.hyunmin.jwt.global.common.repository.MemberRepository;
import com.hyunmin.jwt.global.exception.GeneralException;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberInfoResponseDto changePassword(Long id, ChangePasswordRequestDto requestDto) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        String newEncodedPw = passwordEncoder.encode(requestDto.password());
        member.changePassword(newEncodedPw);
        return MemberInfoResponseDto.from(member);
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        memberRepository.delete(member);
    }
}
