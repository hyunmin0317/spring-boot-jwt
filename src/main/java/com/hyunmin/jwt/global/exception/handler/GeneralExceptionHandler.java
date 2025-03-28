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
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

    // 사용자 정의 예외(GeneralException) 처리 메서드
    @ExceptionHandler(GeneralException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleGeneralException(GeneralException ex) {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());
        return ErrorResponse.handle(ex.getErrorCode());
    }

    // 요청 파라미터 검증 실패(MethodArgumentNotValidException) 처리 메서드
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());
        return ErrorResponse.handle(ErrorCode.VALIDATION_FAILED, ex.getFieldErrors());
    }

    // 데이터 무결성 위반(DataIntegrityViolationException) 처리 메서드
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());
        return ErrorResponse.handle(ErrorCode.VALIDATION_FAILED);
    }

    // 컨트롤러 메서드 파라미터의 유효성 검증 실패(HandlerMethodValidationException) 처리 메서드 - @PermissionCheckValidator
    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());
        return ErrorResponse.handle(extractErrorCode(ex));
    }

    // 지원되지 않는 HTTP 메서드 요청(HttpRequestMethodNotSupportedException) 처리 메서드
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());
        return ErrorResponse.handle(ErrorCode.METHOD_NOT_ALLOWED);
    }

    // 메서드 인자 타입 불일치(MethodArgumentTypeMismatchException) 처리 메서드
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("[WARNING] {} : {}", ex.getClass(), ex.getMessage());
        return ErrorResponse.handle(ErrorCode.INVALID_ENUM_VALUE);
    }

    // 기타 모든 예외(Exception) 처리 메서드
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse<Void>> handleException(Exception ex) {
        log.error("[ERROR] {} : {}", ex.getClass(), ex.getMessage(), ex);
        return ErrorResponse.handle(ErrorCode.INTERNAL_SERVER_ERROR);
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
