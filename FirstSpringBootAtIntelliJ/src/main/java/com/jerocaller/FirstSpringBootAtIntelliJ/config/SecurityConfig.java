package com.jerocaller.FirstSpringBootAtIntelliJ.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] permitAllUris = {
        "/swagger-ui/**",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-ui.html",
        "/swagger-ui/index.html",
        "/api/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
        throws Exception
    {

        httpSecurity
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(permitAllUris).permitAll()
                .anyRequest().authenticated()
            );

        return httpSecurity.build();
    }

}
