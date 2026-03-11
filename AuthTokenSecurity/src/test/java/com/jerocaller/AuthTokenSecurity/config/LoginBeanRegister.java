package com.jerocaller.AuthTokenSecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerocaller.AuthTokenSecurity.util.LoginHelper;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

@TestConfiguration
public class LoginBeanRegister {

    @Bean
    public LoginHelper getLoginBean(
        ObjectMapper objectMapper,
        MockMvc mockMvc,
        JwtAuthenticationProvider jwtAuthenticationProvider
    ) {
        return new LoginHelper(objectMapper, mockMvc, jwtAuthenticationProvider);
    }
}
