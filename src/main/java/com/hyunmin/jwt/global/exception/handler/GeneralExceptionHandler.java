package com.hyunmin.jwt.global.exception.handler;

import com.hyunmin.jwt.global.common.dto.ErrorResponse;
import com.hyunmin.jwt.global.exception.GeneralException;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

    // 사용자 정의 예외(GeneralException) 처리 메서드
    @ExceptionHandler(GeneralException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleGeneralException(GeneralException ex) {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());
        ErrorCode errorCode = ex.getErrorCode();
        return ErrorResponse.handle(errorCode);
    }

    // 요청 파라미터 검증 실패(MethodArgumentNotValidException) 처리 메서드
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());
        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        return ErrorResponse.handle(errorCode, ex.getFieldErrors());
    }

    // 데이터 무결성 위반(DataIntegrityViolationException) 처리 메서드
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());
        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        return ErrorResponse.handle(errorCode);
    }

    // 컨트롤러 메서드 파라미터의 유효성 검증 실패(HandlerMethodValidationException) 처리 메서드 - @PermissionCheckValidator
    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());
        ErrorCode errorCode = extractErrorCode(ex);
        return ErrorResponse.handle(errorCode);
    }

    // Redis 서버 연결 실패(RedisConnectionFailureException) 처리 메서드
    @ExceptionHandler(RedisConnectionFailureException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleRedisConnectionFailureException(RedisConnectionFailureException ex) {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());
        ErrorCode errorCode = ErrorCode.REDIS_CONNECTION_FAILURE;
        return ErrorResponse.handle(errorCode);
    }

    // Redis 서버 오류(REDIS_SYSTEM_EXCEPTION) 처리 메서드
    @ExceptionHandler(RedisSystemException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleRedisSystemException(RedisSystemException ex) {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());
        ErrorCode errorCode = ErrorCode.REDIS_SYSTEM_EXCEPTION;
        return ErrorResponse.handle(errorCode);
    }

    // 기타 모든 예외(Exception) 처리 메서드
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse<Void>> handleException(Exception ex) {
        log.error("[ERROR] {} : {}", ex.getClass(), ex.getMessage());
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ErrorResponse.handle(errorCode);
    }

    // HandlerMethodValidationException 에서 ErrorCode 를 추출하는 메서드
    private ErrorCode extractErrorCode(HandlerMethodValidationException ex) {
        return ex.getAllValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .map(ErrorCode::valueOf)
                .orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
    }
}
