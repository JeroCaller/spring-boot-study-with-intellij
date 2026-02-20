package com.jerocaller.AuthTokenSecurity.exception.custom;

import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;

public class NotAuthenticatedUserException extends BaseCustomException {
    public NotAuthenticatedUserException() {
        super(ResponseCode.NOT_AUTHENTICATED);
    }
}
