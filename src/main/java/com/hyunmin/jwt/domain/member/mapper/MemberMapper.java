package com.hyunmin.jwt.domain.member.mapper;

import com.hyunmin.jwt.domain.member.dto.MemberInfoResponseDto;
import com.hyunmin.jwt.global.common.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(source = "role", target = "memberRole")
    MemberInfoResponseDto toDto(Member member);

    default Page<MemberInfoResponseDto> toDto(Page<Member> memberPage) {
        return memberPage.map(this::toDto);
    }
}
