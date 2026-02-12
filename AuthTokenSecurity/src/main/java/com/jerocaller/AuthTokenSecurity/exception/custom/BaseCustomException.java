package com.jerocaller.AuthTokenSecurity.exception.custom;

import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;
import lombok.Getter;

@Getter
public class BaseCustomException extends RuntimeException {
    protected ResponseCode responseCode;

    public BaseCustomException() {
        super();
    }

    public BaseCustomException(String message) {
        super(message);
    }

    public BaseCustomException(ResponseCode responseCode) {
        super();
        this.responseCode = responseCode;
    }
}
