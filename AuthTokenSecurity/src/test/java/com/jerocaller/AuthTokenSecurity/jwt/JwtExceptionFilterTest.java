package com.jerocaller.AuthTokenSecurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerocaller.AuthTokenSecurity.config.LoginBeanRegister;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;
import com.jerocaller.AuthTokenSecurity.util.LoginHelper;
import com.jerocaller.AuthTokenSecurity.util.TestUtil;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Slf4j
@Import({LoginBeanRegister.class})
class JwtExceptionFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private LoginHelper loginHelper;

    private final String REQUEST_URI = "/api/users/{username}";
    private final UserRequest userRequest = UserRequest.builder()
        .username("imtestingjwt")
        .password("imtestingjwt123")
        .age(20)
        .build();

    @BeforeEach
    void init() throws Exception {
        // 회원가입
        mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest))
        );
    }

    @Test
    @DisplayName("JWT 관련 초기화 테스트")
    void initJwtTest() throws Exception {
        assertThat(jwtAuthenticationProvider.getClass().getSimpleName())
            .isEqualTo(CustomJwtAuthenticationProvider.class.getSimpleName());
        loginHelper.logAuthTokensInfo(loginHelper.login(userRequest));
    }

    @Test
    @DisplayName("JWT access token 만료 후 보호된 자원 요청 시 토큰 만료 응답이 발송된다.")
    void shouldResponseJwtExpired() throws Exception {
        AuthTokensDTO responseAuthTokensDto = loginHelper.login(userRequest);
        loginHelper.logAuthTokensInfo(responseAuthTokensDto);

        // access token을 시간 만료시킨다.
        TestUtil.delay(jwtProperties.getToken().getAccess().getExpiry());

        mockMvc.perform(get(REQUEST_URI, userRequest.getUsername())
            .header(
                JwtAuthenticationProvider.AUTHORIZATION,
                JwtAuthenticationProvider.BEARER + responseAuthTokensDto.getAccessToken()
            )
        )
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(ResponseCode.JWT_EXPIRED.getMessage()))
            .andExpect(jsonPath("$.code").value(ResponseCode.JWT_EXPIRED.getCode()))
            .andDo(print());
    }

    @Test
    @DisplayName("""
        JWT access token의 값을 null 또는 빈 값을 넣어 전달하면 유효하지 않은 토큰 형식이란 응답이 발송된다.
    """)
    void shouldResponseJwtIllegalFormat() throws Exception {
        // 로그인을 하더라도 서버에 요청 시 access token을 넘기지 않으면
        // 유효하지 않은 토큰 형식이란 응답이 발송됨을 증명하기 위해 로그인을 수행함.
        loginHelper.logAuthTokensInfo(loginHelper.login(userRequest));

        ResponseCode responseCode = ResponseCode.JWT_ILLEGAL_ARGUMENT_INSERTED;
        mockMvc.perform(get(REQUEST_URI, userRequest.getUsername())
                .header(JwtAuthenticationProvider.AUTHORIZATION, JwtAuthenticationProvider.BEARER)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(responseCode.getMessage()))
            .andExpect(jsonPath("$.code").value(responseCode.getCode()))
            .andDo(print());
    }
}