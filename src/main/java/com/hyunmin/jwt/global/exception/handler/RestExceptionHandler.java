package com.hyunmin.jwt.global.exception.handler;

import com.hyunmin.jwt.global.common.dto.ErrorResponse;
import com.hyunmin.jwt.global.exception.RestException;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RestException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleRestException(RestException ex) {
        log.warn("[WARNING] Rest Exception : {}", ex.getMessage());
        ErrorCode errorCode = ex.getErrorCode();
        return ErrorResponse.handle(errorCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("[WARNING] Validation Exception : {}", ex.getMessage());
        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        return ErrorResponse.handle(errorCode, ex.getFieldErrors());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse<Void>> handleException(Exception ex) {
        log.error("[ERROR] Internal Server Error : {}", ex.getMessage());
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ErrorResponse.handle(errorCode);
    }
}
