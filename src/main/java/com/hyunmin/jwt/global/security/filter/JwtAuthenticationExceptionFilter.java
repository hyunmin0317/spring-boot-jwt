package com.hyunmin.jwt.global.security.filter;

import com.hyunmin.jwt.global.common.dto.ErrorResponse;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
import com.hyunmin.jwt.global.security.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 예외를 핸들링하는 필터 클래스
 */
public class JwtAuthenticationExceptionFilter extends OncePerRequestFilter {

    /**
     * 요청을 필터링하고, JWT 인증 예외가 발생하면 오류 응답 설정
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            // 필터 체인 계속 실행
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException exception) {
            // 예외 발생 시 오류 응답 설정
            handleException(response, exception);
        }
    }

    // JwtAuthenticationException 발생 시 오류 응답 설정
    private void handleException(HttpServletResponse response, JwtAuthenticationException exception) throws IOException {
        // ErrorCode 객체로부터 오류 응답 생성
        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse<Object> errorResponse = ErrorResponse.from(errorCode);

        // 응답의 Content-Type, 문자 인코딩, 상태 코드 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        // ErrorResponse 객체를 JSON 문자열로 변환하여 응답 본문에 작성
        response.getWriter().write(errorResponse.toJsonString());
    }
}
