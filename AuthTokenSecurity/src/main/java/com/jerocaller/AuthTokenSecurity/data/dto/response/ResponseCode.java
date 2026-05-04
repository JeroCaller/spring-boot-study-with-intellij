package com.jerocaller.AuthTokenSecurity.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    OK(HttpStatus.OK, "OK", "응답 성공"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "존재하지 않는 유저입니다."),
    INVALID_REQUEST_FORMAT(
        HttpStatus.BAD_REQUEST,
        "INVALID_REQUEST_FORMAT",
        "유효하지 않는 입력 형식입니다."
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
    NOT_AUTHORIZED(
        HttpStatus.UNAUTHORIZED,
        "NOT_AUTHORIZED",
        "접근 권한이 없어 접근이 거부되었습니다."
    ),
    PASSWORD_NOT_MATCHED(
        HttpStatus.CONFLICT,
        "PASSWORD_NOT_MATCHED",
        "비밀번호가 일치하지 않습니다. 다시 시도해주세요."
    ),
    RELOGIN_REQUIRED(
        HttpStatus.UNAUTHORIZED,
        "RELOGIN_REQUIRED",
        "재로그인이 필요합니다. 다시 인증하세요."
    ),
    INTERNAL_SERVER_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "INTERNAL_SERVER_ERROR",
        "서버 내부에서 문제가 발생하였습니다."
    ),
    JWT_UNKNOWN_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "JWT_UNKNOWN_ERROR",
        "처리되지 않은 JWT 관련 예외가 발생하였습니다."
    ),
    JWT_EXPIRED(
        HttpStatus.UNAUTHORIZED,
        "JWT_EXPIRED",
        "인증 토큰이 만료되었습니다. 재인증을 위해 새 토큰으로 갱신해야합니다."
    ),
    REFRESH_TOKEN_EXPIRED(
        HttpStatus.UNAUTHORIZED,
        "REFRESH_TOKEN_EXPIRED",
        "refresh token이 만료되었습니다. 다시 로그인 해주세요."
    ),
    JWT_ILLEGAL_ARGUMENT_INSERTED(
        HttpStatus.BAD_REQUEST,
        "JWT_ILLEGAL_ARGUMENT_INSERTED",
        "null 또는 빈 JWT 토큰이 입력되었습니다. 유효한 형식의 JWT 토큰을 입력하세요."
    ),
    JWT_UNEXPECTED_FORMAT(
        HttpStatus.BAD_REQUEST,
        "JWT_UNEXPECTED_FORMAT",
        "지원되지 않는 형식의 JWT 토큰입니다."
    )
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
