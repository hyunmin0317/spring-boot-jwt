package com.hyunmin.jwt.global.security.provider;

import com.hyunmin.jwt.global.common.entity.enums.MemberRole;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
import com.hyunmin.jwt.global.security.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * JWT 토큰을 생성하고 검증하는 클래스
 */
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final Long accessExpirationTime;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token.access-expiration-time}") long accessExpirationTime) {
        this.secretKey = generateSecretKeySpec(secret);
        this.accessExpirationTime = accessExpirationTime;
    }

    /**
     * JWT access 토큰 생성
     */
    public String createAccessToken(Long memberId, MemberRole memberRole) {
        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .claim("memberRole", memberRole.name())
                .signWith(secretKey)
                .expiration(expirationDate())
                .compact();
    }

    /**
     * 요청 헤더에서 JWT 토큰 추출
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ") ?
                bearerToken.substring(7) : null;
    }

    /**
     * JWT 토큰 유효성 검증
     *
     * @param isRefresh JWT refresh 토큰인지 여부
     */
    public boolean validateToken(String token, boolean isRefresh) {
        if (!StringUtils.hasText(token)) return false;
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException(isRefresh ? ErrorCode.RELOGIN_EXCEPTION : ErrorCode.EXPIRED_JWT_EXCEPTION);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN_EXCEPTION);
        }
    }

    /**
     * JWT 토큰을 기반으로 Authentication 객체 생성
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String memberRole = claims.get("memberRole").toString();
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(memberRole));
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 비밀 키 생성
    private SecretKey generateSecretKeySpec(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 액세스 토큰의 만료 시간 계산
    private Date expirationDate() {
        Date now = new Date();
        return new Date(now.getTime() + this.accessExpirationTime);
    }

    // JWT 토큰에서 클레임을 추출
    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
