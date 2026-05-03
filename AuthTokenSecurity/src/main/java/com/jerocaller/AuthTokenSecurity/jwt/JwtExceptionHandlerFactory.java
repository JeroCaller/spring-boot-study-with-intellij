package com.jerocaller.AuthTokenSecurity.jwt;

import io.jsonwebtoken.JwtException;

import java.util.HashMap;
import java.util.Map;

public class JwtExceptionHandlerFactory {
    private static final Map<Class<? extends JwtException>, JwtExceptionHandler> handlerMapper =
        new HashMap<>();

    public void setDefaultHandler(JwtExceptionHandler handler) {
        handlerMapper.put(JwtException.class, handler);
    }

    public void register(Class<? extends JwtException> jwtException, JwtExceptionHandler handler) {
        handlerMapper.put(jwtException, handler);
    }

    public JwtExceptionHandler getHandler(Class<? extends JwtException> jwtException) {
        if (handlerMapper.containsKey(jwtException)) {
            return handlerMapper.get(jwtException);
        }

        return handlerMapper.get(JwtException.class);
    }
}
