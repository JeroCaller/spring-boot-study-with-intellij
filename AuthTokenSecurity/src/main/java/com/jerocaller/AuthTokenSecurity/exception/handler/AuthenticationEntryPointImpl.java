package com.jerocaller.AuthTokenSecurity.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;
import com.jerocaller.AuthTokenSecurity.data.dto.response.RestResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException
    {
        // TODO - refactoring using RestResponse
        RestResponse<Object> restResponse = RestResponse.builder()
            .responseCode(ResponseCode.AUTHENTICATION_FAILED)
            .build();
        RestResponse.DetailedRestResponse<Object> detailedRestResponse = RestResponse
            .DetailedRestResponse
            .toDetailedRestResponse(restResponse);

        response.setStatus(restResponse.getResponseCode().getHttpStatus().value());
        response.setContentType("application/json");  // TODO - refactoring using HttpResponseUtil
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(
            objectMapper.writeValueAsString(detailedRestResponse)
        );
    }
}
