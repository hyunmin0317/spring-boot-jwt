package com.hyunmin.jwt.domain.member.service;

import com.hyunmin.jwt.domain.member.dto.ChangePasswordRequest;
import com.hyunmin.jwt.domain.member.dto.MemberInfoResponse;
import com.hyunmin.jwt.domain.member.mapper.MemberMapper;
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

    public MemberInfoResponse changePassword(Long id, ChangePasswordRequest request) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        String newEncodedPw = passwordEncoder.encode(request.password());
        member.changePassword(newEncodedPw);
        return MemberMapper.INSTANCE.toResponse(member);
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        memberRepository.delete(member);
    }
}
