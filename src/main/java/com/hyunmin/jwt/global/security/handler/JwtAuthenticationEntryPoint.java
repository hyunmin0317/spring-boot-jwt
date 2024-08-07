package com.hyunmin.jwt.global.security.handler;

import com.hyunmin.jwt.global.exception.code.ErrorCode;
import com.hyunmin.jwt.global.security.exception.handler.JwtAuthenticationExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * JWT 인증 실패 시 오류 응답을 보내는 EntryPoint 클래스
 * JWT 인증 시 인증 정보가 없거나 잘못된 경우
 */
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 인증 실패 시 호출되는 메서드
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        JwtAuthenticationExceptionHandler.handleException(response, ex, ErrorCode.UNAUTHORIZED_EXCEPTION);
    }
}