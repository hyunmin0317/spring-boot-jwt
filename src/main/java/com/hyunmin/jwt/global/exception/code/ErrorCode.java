package com.hyunmin.jwt.global.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common Errors
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON000", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON001", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON002", "로그인이 필요합니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON003", "지원하지 않는 Http Method 입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON004", "금지된 요청입니다."),

    // Validation Errors
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALID001", "입력값에 대한 검증에 실패했습니다."),

    // Account Errors
    ACCOUNT_CONFLICT(HttpStatus.CONFLICT, "ACCOUNT001", "중복된 사용자 이름 입니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT002", "해당 사용자를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "ACCOUNT003", "비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT004", "토큰이 올바르지 않습니다."),
    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT005", "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
    RELOGIN_EXCEPTION(HttpStatus.UNAUTHORIZED, "AUTH005", "모든 토큰이 만료되었습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "AUTH003", "로그인 후 이용가능합니다. 토큰을 입력해 주세요");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
