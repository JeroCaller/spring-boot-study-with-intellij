package com.jerocaller.AuthTokenSecurity.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    OK(HttpStatus.OK, "OK", "응답 성공"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "존재하지 않는 유저입니다."),
    INVALID_USER_FORMAT(
        HttpStatus.BAD_REQUEST,
        "INVALID_USER_FORMAT",
        "유효하지 않은 유저 정보 형식입니다."
    ),
    USERNAME_ALREADY_EXISTS(
        HttpStatus.CONFLICT,
        "USERNAME_ALREADY_EXSISTS",
        "이미 존재하는 유저 네임입니다. 다른 이름을 사용해주세요"
    )
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
