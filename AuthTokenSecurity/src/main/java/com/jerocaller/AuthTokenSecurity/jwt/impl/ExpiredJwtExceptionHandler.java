package com.jerocaller.AuthTokenSecurity.jwt.impl;

import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;
import com.jerocaller.AuthTokenSecurity.data.dto.response.RestResponse;
import com.jerocaller.AuthTokenSecurity.jwt.JwtExceptionHandler;
import jakarta.servlet.http.HttpServletResponse;

public class ExpiredJwtExceptionHandler implements JwtExceptionHandler {

    @Override
    public RestResponse.DetailedRestResponse<Object> handle(HttpServletResponse response) {
        ResponseCode responseCode = ResponseCode.JWT_EXPIRED;
        response.setStatus(responseCode.getHttpStatus().value());
        return RestResponse.builder()
            .responseCode(responseCode)
            .build()
            .toDetailedRestResponse();
    }
}
