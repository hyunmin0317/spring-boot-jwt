package com.hyunmin.jwt.global.security.provider;

import com.hyunmin.jwt.domain.account.entity.enums.MemberRole;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenProvider {

    private final SecretKey secretKey;
    private final Long accessExpirationTime;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token.access-expiration-time}") long accessExpirationTime) {
        this.secretKey = generateSecretKeySpec(secret);
        this.accessExpirationTime = accessExpirationTime;
    }

    public String createAccessToken(String username, MemberRole memberRole) {
        return Jwts.builder()
                .subject(username)
                .claim("memberRole", memberRole.name())
                .signWith(secretKey)
                .expiration(expirationDate())
                .compact();
    }

    private SecretKey generateSecretKeySpec(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    private Date expirationDate() {
        Date now = new Date();
        return new Date(now.getTime() + this.accessExpirationTime);
    }
}
