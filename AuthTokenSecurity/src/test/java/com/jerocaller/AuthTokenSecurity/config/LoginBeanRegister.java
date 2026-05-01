package com.jerocaller.AuthTokenSecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerocaller.AuthTokenSecurity.util.LoginHelper;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * <p>
 *     테스트 클래스에서 <code>LoginHelper</code>를 Bean으로 주입받아 사용하고자 할 때
 *     이 설정 클래스를 import 해야 한다. 테스트 클래스 위에 {@code @Import({LoginBeanRegister.class})}
 *     라고 등록한다.
 * </p>
 */
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
