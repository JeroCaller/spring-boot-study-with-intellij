package com.jerocaller.AuthTokenSecurity.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtExceptionHandlerFactory {
    private final Map<Class<? extends JwtException>, JwtExceptionHandler> handlerMapper =
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
