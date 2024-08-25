package com.hyunmin.jwt.domain.account.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private String token;

    private Long memberId;

    public static RefreshToken of(Long memberId, String token) {
        return RefreshToken.builder()
                .memberId(memberId)
                .token(token)
                .build();
    }
}
