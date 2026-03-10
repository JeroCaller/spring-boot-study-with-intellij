package com.jerocaller.AuthTokenSecurity.exception.custom.jwt;

import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;
import com.jerocaller.AuthTokenSecurity.exception.custom.BaseCustomException;

public class RefreshTokenExpiredException extends BaseCustomException {
    public RefreshTokenExpiredException() {
        super(ResponseCode.REFRESH_TOKEN_EXPIRED);
    }
}
