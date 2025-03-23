package com.hyunmin.jwt.domain.member.controller;

import com.hyunmin.jwt.domain.member.dto.ChangePasswordRequest;
import com.hyunmin.jwt.domain.member.dto.MemberInfoResponse;
import com.hyunmin.jwt.domain.member.service.MemberCommandService;
import com.hyunmin.jwt.domain.member.service.MemberQueryService;
import com.hyunmin.jwt.global.security.annotation.AuthMember;
import com.hyunmin.jwt.global.validation.annotation.PermissionCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping
    public ResponseEntity<Page<MemberInfoResponse>> findAll(@ParameterObject Pageable pageable) {
        Page<MemberInfoResponse> responses = memberQueryService.findAll(pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberInfoResponse> findById(@PermissionCheck @PathVariable Long id) {
        MemberInfoResponse response = memberQueryService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@AuthMember Long memberId) {
        MemberInfoResponse response = memberQueryService.findById(memberId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<MemberInfoResponse> changePassword(@AuthMember Long memberId,
                                                             @RequestBody @Valid ChangePasswordRequest request) {
        MemberInfoResponse response = memberCommandService.changePassword(memberId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@AuthMember Long memberId) {
        memberCommandService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
