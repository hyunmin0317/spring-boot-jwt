package com.hyunmin.jwt.domain.member.controller;

import com.hyunmin.jwt.domain.member.dto.MemberInfoResponseDto;
import com.hyunmin.jwt.domain.member.service.MemberQueryService;
import com.hyunmin.jwt.global.common.entity.Member;
import com.hyunmin.jwt.global.security.annotation.AuthMember;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberQueryService memberQueryService;

    @GetMapping
    public ResponseEntity<Page<MemberInfoResponseDto>> findAll(@ParameterObject Pageable pageable) {
        Page<MemberInfoResponseDto> responseDtoPage = memberQueryService.findAll(pageable);
        return ResponseEntity.ok(responseDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberInfoResponseDto> findById(@PathVariable Long id) {
        MemberInfoResponseDto responseDto = memberQueryService.findById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponseDto> memberInfo(@AuthMember Member member) {
        MemberInfoResponseDto responseDto = MemberInfoResponseDto.from(member);
        return ResponseEntity.ok(responseDto);
    }
}
