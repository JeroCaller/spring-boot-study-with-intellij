package com.jerocaller.AuthTokenSecurity.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    OK(HttpStatus.OK, "OK", "응답 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
