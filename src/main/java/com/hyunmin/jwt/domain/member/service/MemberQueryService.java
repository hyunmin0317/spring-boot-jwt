package com.hyunmin.jwt.domain.member.service;

import com.hyunmin.jwt.domain.member.dto.MemberInfoResponse;
import com.hyunmin.jwt.domain.member.mapper.MemberMapper;
import com.hyunmin.jwt.global.common.entity.Member;
import com.hyunmin.jwt.global.common.repository.MemberRepository;
import com.hyunmin.jwt.global.exception.GeneralException;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public Page<MemberInfoResponse> findAll(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return MemberMapper.INSTANCE.toResponse(memberPage);
    }

    public MemberInfoResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        return MemberMapper.INSTANCE.toResponse(member);
    }
}
