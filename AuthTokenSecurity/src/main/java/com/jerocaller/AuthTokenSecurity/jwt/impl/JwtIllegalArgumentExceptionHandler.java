package com.jerocaller.AuthTokenSecurity.jwt.impl;

import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;
import com.jerocaller.AuthTokenSecurity.data.dto.response.RestResponse;
import com.jerocaller.AuthTokenSecurity.exception.custom.jwt.JwtIllegalArgumentException;
import com.jerocaller.AuthTokenSecurity.jwt.JwtExceptionHandler;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class JwtIllegalArgumentExceptionHandler implements JwtExceptionHandler {

    @Override
    public Object handle(HttpServletResponse response) {
        ResponseCode responseCode = ResponseCode.JWT_ILLEGAL_ARGUMENT_INSERTED;
        response.setStatus(responseCode.getHttpStatus().value());
        return RestResponse.builder()
            .responseCode(responseCode)
            .build()
            .toDetailedRestResponse();
    }

    @Override
    public Class<? extends JwtException> getJwtExceptionClass() {
        return JwtIllegalArgumentException.class;
    }
}
