package com.hyunmin.jwt.domain.member.controller;

import com.hyunmin.jwt.domain.member.dto.MemberInfoResponseDto;
import com.hyunmin.jwt.global.common.entity.Member;
import com.hyunmin.jwt.global.security.annotation.AuthMember;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    @GetMapping("/info")
    public MemberInfoResponseDto memberInfo(@AuthMember Member member) {
        return MemberInfoResponseDto.from(member);
    }
}
