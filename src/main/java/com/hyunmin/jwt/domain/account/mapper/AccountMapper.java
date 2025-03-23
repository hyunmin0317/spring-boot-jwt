package com.hyunmin.jwt.domain.account.mapper;

import com.hyunmin.jwt.domain.account.dto.LoginResponse;
import com.hyunmin.jwt.domain.account.dto.RegisterRequest;
import com.hyunmin.jwt.domain.account.dto.RegisterResponse;
import com.hyunmin.jwt.global.common.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", source = "encodedPw")
    Member toEntity(RegisterRequest request, String encodedPw);

    RegisterResponse toResponse(Member member);

    default LoginResponse toResponse(Long memberId, String accessToken, String refreshToken) {
        return new LoginResponse(memberId, accessToken, refreshToken);
    }
}
