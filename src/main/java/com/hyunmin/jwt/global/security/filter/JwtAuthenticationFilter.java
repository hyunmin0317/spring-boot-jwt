package com.hyunmin.jwt.global.security.filter;

import com.hyunmin.jwt.global.security.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 기반의 인증을 처리하는 필터 클래스
 * Spring Security의 OncePerRequestFilter를 상속받아 매 요청마다 한번만 실행됨
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 요청을 필터링하여 JWT 토큰을 검증 후 인증 정보를 설정
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 요청에서 JWT 토큰 추출
        String jwt = jwtTokenProvider.resolveToken(request);

        // JWT 토큰이 존재하고 유효한지 검증 후 인증 정보를 가져와서 설정
        Authentication authentication = StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt, false) ?
                jwtTokenProvider.getAuthentication(jwt) : null;

        // 인증 정보를 SecurityContext에 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 필터 체인을 통해 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }
}
