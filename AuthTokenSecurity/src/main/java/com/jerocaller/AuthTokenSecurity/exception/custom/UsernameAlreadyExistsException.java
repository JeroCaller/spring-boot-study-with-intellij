package com.jerocaller.AuthTokenSecurity.exception.custom;

import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;

public class UsernameAlreadyExistsException extends BaseCustomException{
    public UsernameAlreadyExistsException() {
        super(ResponseCode.USERNAME_ALREADY_EXISTS);
    }
}
