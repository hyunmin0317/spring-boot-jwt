package com.hyunmin.jwt.domain.member.controller;

import com.hyunmin.jwt.domain.member.dto.MemberInfoResponseDto;
import com.hyunmin.jwt.global.common.entity.Member;
import com.hyunmin.jwt.global.common.service.MemberQueryService;
import com.hyunmin.jwt.global.security.annotation.AuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberQueryService memberQueryService;

    @GetMapping
    public Page<MemberInfoResponseDto> findAll(@PageableDefault(size = 5) Pageable pageable) {
        return memberQueryService.findAll(pageable);
    }

    @GetMapping("/info")
    public MemberInfoResponseDto memberInfo(@AuthMember Member member) {
        return MemberInfoResponseDto.from(member);
    }
}
