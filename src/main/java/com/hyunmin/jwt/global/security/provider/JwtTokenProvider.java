package com.hyunmin.jwt.global.security.provider;

import com.hyunmin.jwt.global.common.entity.enums.MemberRole;
import com.hyunmin.jwt.global.exception.RestException;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
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

    public String createAccessToken(String username, MemberRole memberRole) {
        return Jwts.builder()
                .subject(username)
                .claim("memberRole", memberRole.name())
                .signWith(secretKey)
                .expiration(expirationDate())
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ") ?
                bearerToken.substring(7) : null;
    }

    public boolean validateToken(String token, boolean isRefresh) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new RestException(isRefresh ? ErrorCode.RELOGIN_EXCEPTION : ErrorCode.EXPIRED_JWT_EXCEPTION);
        } catch (JwtException | IllegalArgumentException e) {
            throw new RestException(ErrorCode.INVALID_TOKEN_EXCEPTION);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String memberRole = claims.get("memberRole").toString();
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(memberRole));
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private SecretKey generateSecretKeySpec(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    private Date expirationDate() {
        Date now = new Date();
        return new Date(now.getTime() + this.accessExpirationTime);
    }

    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
