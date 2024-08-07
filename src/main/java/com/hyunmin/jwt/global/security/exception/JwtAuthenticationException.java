package com.hyunmin.jwt.global.security.exception;

import com.hyunmin.jwt.global.exception.GeneralException;
import com.hyunmin.jwt.global.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class JwtAuthenticationException extends GeneralException {

    public JwtAuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
