package com.jerocaller.AuthTokenSecurity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.dto.request.AuthRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;
import com.jerocaller.AuthTokenSecurity.data.entity.AuthToken;
import com.jerocaller.AuthTokenSecurity.data.entity.User;
import com.jerocaller.AuthTokenSecurity.data.repository.AuthTokenRepository;
import com.jerocaller.AuthTokenSecurity.data.repository.UserRepository;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthTokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private JwtProperties jwtProperties;

    private final String LOGIN_URI = "/api/auth/login";
    private final UserRequest userRequest = UserRequest.builder()
        .username("gugudan123")
        .password("gugudan123")
        .age(25)
        .build();

    @BeforeEach
    void init() throws Exception {
        mockMvc.perform(post("/api/users")
            .content(objectMapper.writeValueAsString(userRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    @DisplayName("초기화 테스트")
    void initTest() {
        Optional<User> optUser = userRepository.findByUsername(userRequest.getUsername());
        assertThat(optUser.isPresent()).isTrue();
        User user = optUser.get();
        assertThat(user.getUsername()).isEqualTo(userRequest.getUsername());
        assertThat(user.getAge()).isEqualTo(userRequest.getAge());
        assertThat(user.getLastLoginAt()).isNull();
    }

    @Test
    @DisplayName("JWT 관련 초기화 테스트")
    void initJwtTest() {
        assertThat(jwtProperties.getToken().getAccess().getCookieName())
            .isEqualTo("ACCESS-TOKEN-TEST");
        assertThat(jwtProperties.getToken().getRefresh().getCookieName())
            .isEqualTo("REFRESH-TOKEN-TEST");
    }

    @Test
    @DisplayName("로그인 성공 여부 테스트")
    void testLoginSuccess() throws Exception {
        AuthRequest authRequest = AuthRequest.builder()
            .username(userRequest.getUsername())
            .password(userRequest.getPassword())
            .build();

        final MvcResult mvcResult = mockMvc.perform(post(LOGIN_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.httpStatus")
                .value(ResponseCode.OK.getHttpStatus().getReasonPhrase())
            )
            .andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andExpect(jsonPath("$.data.refreshToken").exists())
            .andDo(print())
            .andReturn();

        User user = userRepository.findByUsername(userRequest.getUsername()).get();
        assertThat(user.getLastLoginAt()).isNotNull();

        AuthTokensDTO responseTokens = AuthTokensDTO.builder()
            .accessToken(JsonPath.read(
                mvcResult.getResponse().getContentAsString(),
                "$.data.accessToken")
            )
            .refreshToken(JsonPath.read(
                mvcResult.getResponse().getContentAsString(),
                "$.data.refreshToken")
            )
            .build();
        assertThat(responseTokens.getAccessToken()).isNotNull();
        assertThat(responseTokens.getRefreshToken()).isNotNull();
        jwtTokenTest(responseTokens.getAccessToken());
        jwtTokenTest(responseTokens.getRefreshToken());

        Optional<AuthToken> optionalAuthToken = authTokenRepository
            .findByRefreshToken(responseTokens.getRefreshToken());
        assertThat(optionalAuthToken.isPresent()).isTrue();
        AuthToken authToken = optionalAuthToken.get();
        assertThat(authToken.isValid()).isTrue();
    }

    @Test
    @DisplayName("로그아웃 성공 테스트")
    void testLogoutSuccess() throws Exception {
        AuthRequest authRequest = AuthRequest.builder()
            .username(userRequest.getUsername())
            .password(userRequest.getPassword())
            .build();

        final MvcResult mvcResult = mockMvc.perform(post(LOGIN_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
            )
            .andExpect(status().isOk())
            .andReturn();

        String responseRefreshToken = JsonPath.read(
            mvcResult.getResponse().getContentAsString(),
            "$.data.refreshToken"
        );

        final String refreshTokenCookieName = jwtProperties.getToken().getRefresh().getCookieName();
        mockMvc.perform(post("/api/auth/logout")
            .cookie(new Cookie(refreshTokenCookieName, responseRefreshToken))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").doesNotExist())
            .andDo(print())
            .andReturn();

        Optional<AuthToken> optionalAuthToken = authTokenRepository
            .findByPreviousRefreshToken(responseRefreshToken);
        assertThat(optionalAuthToken.isPresent()).isTrue();
        AuthToken authTokenEntity = optionalAuthToken.get();
        assertThat(authTokenEntity.isValid()).isFalse();
        assertThat(authTokenEntity.getRefreshToken()).isNull();
    }

    private void jwtTokenTest(String jwtToken) {
        assertThat(jwtAuthenticationProvider.validateToken(jwtToken)).isTrue();
        Claims claims = jwtAuthenticationProvider.extractClaims(jwtToken);
        assertThat(claims.getSubject()).isEqualTo(userRequest.getUsername());
    }
}