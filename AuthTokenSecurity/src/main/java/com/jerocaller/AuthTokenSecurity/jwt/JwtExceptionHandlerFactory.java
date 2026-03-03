package com.jerocaller.AuthTokenSecurity.jwt;

import io.jsonwebtoken.JwtException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtExceptionHandlerFactory {
    private static final JwtExceptionHandlerFactory jwtExceptionHandlerFactory =
        new JwtExceptionHandlerFactory();
    private static final Map<Class<? extends JwtException>, JwtExceptionHandler> handlerMapper =
        new HashMap<>();

    public static JwtExceptionHandlerFactory getInstance() {
        return jwtExceptionHandlerFactory;
    }

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
