package com.jerocaller.AuthTokenSecurity.exception.custom.jwt;

import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;
import com.jerocaller.AuthTokenSecurity.exception.custom.BaseCustomException;

public class JwtUnexpectedFormatException extends BaseCustomException {
    public JwtUnexpectedFormatException() {
        super(ResponseCode.JWT_UNEXPECTED_FORMAT);
    }
}
