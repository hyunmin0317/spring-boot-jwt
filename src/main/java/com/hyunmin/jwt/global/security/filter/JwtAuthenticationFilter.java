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

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

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
