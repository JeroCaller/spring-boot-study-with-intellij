package com.jerocaller.AuthTokenSecurity.exception.custom;

import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;

public class PasswordNotMatchException extends BaseCustomException {
    public PasswordNotMatchException() {
        super(ResponseCode.PASSWORD_NOT_MATCHED);
    }
}
