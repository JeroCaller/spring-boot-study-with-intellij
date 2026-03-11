package com.jerocaller.AuthTokenSecurity.jwt;

import jakarta.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface JwtExceptionHandler {
    Object handle(HttpServletResponse response);
}
