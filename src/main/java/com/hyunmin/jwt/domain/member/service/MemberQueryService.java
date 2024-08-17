package com.hyunmin.jwt.domain.member.service;

import com.hyunmin.jwt.domain.member.dto.MemberInfoResponseDto;
import com.hyunmin.jwt.global.common.entity.Member;
import com.hyunmin.jwt.global.common.repository.MemberRepository;
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

    public Page<MemberInfoResponseDto> findAll(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return MemberInfoResponseDto.from(memberPage);
    }
}
