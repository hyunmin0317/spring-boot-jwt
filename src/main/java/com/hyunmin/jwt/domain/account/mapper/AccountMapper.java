package com.hyunmin.jwt.domain.account.mapper;

import com.hyunmin.jwt.domain.account.dto.RegisterRequestDto;
import com.hyunmin.jwt.domain.account.dto.RegisterResponseDto;
import com.hyunmin.jwt.global.common.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    RegisterResponseDto toDto(Member member);

    @Mapping(target = "password", source = "encodedPw")
    Member toEntity(RegisterRequestDto requestDto, String encodedPw);
}
