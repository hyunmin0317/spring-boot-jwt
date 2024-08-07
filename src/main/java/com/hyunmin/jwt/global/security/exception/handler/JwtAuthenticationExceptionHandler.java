package com.hyunmin.jwt.global.security.exception.handler;

import com.hyunmin.jwt.global.common.dto.ErrorResponse;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
import com.hyunmin.jwt.global.security.exception.JwtAuthenticationException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * JWT 인증 관련 예외를 처리하는 핸들러 클래스
 */
@Slf4j
public class JwtAuthenticationExceptionHandler {

    // JwtAuthenticationException 발생 시 오류 응답 설정
    public static void handleException(HttpServletResponse response, JwtAuthenticationException ex) throws IOException {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());

        // ErrorCode 객체로부터 오류 응답 생성
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse<Object> errorResponse = ErrorResponse.from(errorCode);

        // 응답의 Content-Type, 문자 인코딩, 상태 코드 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        // ErrorResponse 객체를 JSON 문자열로 변환하여 응답 본문에 작성
        response.getWriter().write(errorResponse.toJsonString());
    }
}
