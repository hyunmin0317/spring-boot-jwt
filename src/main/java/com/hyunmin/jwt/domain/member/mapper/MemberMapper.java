package com.hyunmin.jwt.domain.member.mapper;

import com.hyunmin.jwt.domain.member.dto.MemberInfoResponse;
import com.hyunmin.jwt.global.common.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target = "memberRole", source = "role")
    MemberInfoResponse toResponse(Member member);

    default Page<MemberInfoResponse> toResponse(Page<Member> memberPage) {
        return memberPage.map(this::toResponse);
    }
}
