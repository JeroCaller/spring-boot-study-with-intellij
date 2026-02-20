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
    ),
    AUTHENTICATION_FAILED(
        HttpStatus.UNAUTHORIZED,
        "AUTHENTICATION_FAILED",
        "인증에 실패하였습니다. 입력한 정보를 다시 확인해주세요."
    ),
    NOT_AUTHENTICATED(
        HttpStatus.UNAUTHORIZED,
        "NOT_AUTHENTICATED",
        "미인증자는 접근할 수 없습니다. 인증을 먼저 하세요"
    ),
    PASSWORD_NOT_MATCHED(
        HttpStatus.CONFLICT,
        "PASSWORD_NOT_MATCHED",
        "비밀번호가 일치하지 않습니다. 다시 시도해주세요."
    ),
    INTERNAL_SERVER_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "INTERNAL_SERVER_ERROR",
        "서버 내부에서 문제가 발생하였습니다."
    )
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
