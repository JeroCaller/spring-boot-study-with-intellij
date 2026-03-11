package com.jerocaller.AuthTokenSecurity.exception.custom.jwt;

import io.jsonwebtoken.JwtException;

public class JwtIllegalArgumentException extends JwtException {
    public JwtIllegalArgumentException(String message) {
        super(message);
    }

    public JwtIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
