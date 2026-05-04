package com.jerocaller.AuthTokenSecurity.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtExceptionHandler {
    Object handle(HttpServletResponse response);

    /**
     * <p>
     *     현재 handler와 매핑할 JwtException 클래스를 반환한다.
     *     이 메서드로부터 반환되는 JwtException 클래스와 현재 핸들러 객체와 매핑되어 등록된다.
     * </p>
     *
     * @return
     */
    Class<? extends JwtException> getJwtExceptionClass();
}
