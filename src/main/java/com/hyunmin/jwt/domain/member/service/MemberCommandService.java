package com.hyunmin.jwt.domain.member.service;

import com.hyunmin.jwt.global.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
