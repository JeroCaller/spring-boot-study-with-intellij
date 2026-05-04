package com.jerocaller.AuthTokenSecurity.config;

import com.jerocaller.AuthTokenSecurity.jwt.JwtExceptionHandler;
import com.jerocaller.AuthTokenSecurity.jwt.JwtExceptionHandlerFactory;
import com.jerocaller.AuthTokenSecurity.jwt.impl.DefaultJwtExceptionHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
    private final JwtExceptionHandlerFactory factory;
    private final List<JwtExceptionHandler> handlers;

    @PostConstruct
    public void registerHandlers() {
        factory.setDefaultHandler(getDefaultHandler());

        for (JwtExceptionHandler handler : getHandlersExceptDefault()) {
            factory.register(handler.getJwtExceptionClass(), handler);
        }
    }

    private JwtExceptionHandler getDefaultHandler() {
        for (JwtExceptionHandler handler : handlers) {
            if (handler.getClass().getSimpleName()
                .equals(DefaultJwtExceptionHandler.class.getSimpleName())
            ) {
                return handler;
            }
        }

        return null;
    }

    private List<JwtExceptionHandler> getHandlersExceptDefault() {
        List<JwtExceptionHandler> filteredHandlers = new ArrayList<>();

        for (JwtExceptionHandler handler : handlers) {
            if (handler.getClass().getSimpleName()
                .equals(DefaultJwtExceptionHandler.class.getSimpleName())
            ) {
                continue;
            }

            filteredHandlers.add(handler);
        }

        return filteredHandlers;
    }
}
