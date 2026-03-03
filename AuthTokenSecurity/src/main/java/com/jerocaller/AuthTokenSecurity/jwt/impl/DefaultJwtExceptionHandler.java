package com.jerocaller.AuthTokenSecurity.jwt.impl;

import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;
import com.jerocaller.AuthTokenSecurity.data.dto.response.RestResponse;
import com.jerocaller.AuthTokenSecurity.jwt.JwtExceptionHandler;
import jakarta.servlet.http.HttpServletResponse;

public class DefaultJwtExceptionHandler implements JwtExceptionHandler {

    @Override
    public Object handle(HttpServletResponse response) {
        ResponseCode responseCode = ResponseCode.JWT_UNKNOWN_ERROR;
        response.setStatus(responseCode.getHttpStatus().value());
        return RestResponse.builder()
            .responseCode(responseCode)
            .build()
            .toDetailedRestResponse();
    }
}
