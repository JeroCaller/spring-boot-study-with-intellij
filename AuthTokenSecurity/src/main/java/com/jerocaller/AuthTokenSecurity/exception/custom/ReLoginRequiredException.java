package com.jerocaller.AuthTokenSecurity.exception.custom;

import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;

public class ReLoginRequiredException extends BaseCustomException {
    public ReLoginRequiredException() {
        super(ResponseCode.RELOGIN_REQUIRED);
    }
}
