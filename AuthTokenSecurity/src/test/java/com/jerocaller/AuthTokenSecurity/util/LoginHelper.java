package com.jerocaller.AuthTokenSecurity.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.dto.request.AuthRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RequiredArgsConstructor
@Slf4j
public class LoginHelper {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public static final String LOGIN_URL = "/api/auth/login";

    @PostConstruct
    private void init() {
        log.info("{} 인스턴스가 생성되었습니다.", this.getClass().getSimpleName());
    }

    public AuthTokensDTO login(UserRequest userRequest) throws Exception {
        AuthRequest authRequest = AuthRequest.builder()
            .username(userRequest.getUsername())
            .password(userRequest.getPassword())
            .build();
        final MvcResult loginResult = mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
            )
            .andReturn();

        return AuthTokensDTO.builder()
            .accessToken(JsonPath.read(
                loginResult.getResponse().getContentAsString(),
                "$.data.accessToken"
            ))
            .refreshToken(JsonPath.read(
                loginResult.getResponse().getContentAsString(),
                "$.data.refreshToken"
            ))
            .build();
    }

    public void logAuthTokensInfo(AuthTokensDTO authTokensDTO) {
        Claims accessTokenClaims = jwtAuthenticationProvider
            .extractClaims(authTokensDTO.getAccessToken());
        Claims refreshTokenClaims = jwtAuthenticationProvider
            .extractClaims(authTokensDTO.getRefreshToken());
        log.info("The access token expires in {} ms",
            accessTokenClaims.getExpiration().toInstant().toEpochMilli() -
                System.currentTimeMillis()
        );
        log.info("The refresh token expires in {} ms",
            refreshTokenClaims.getExpiration().toInstant().toEpochMilli() -
                System.currentTimeMillis()
        );
    }
}
