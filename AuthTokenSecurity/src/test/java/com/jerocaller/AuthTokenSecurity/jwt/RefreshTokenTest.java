package com.jerocaller.AuthTokenSecurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerocaller.AuthTokenSecurity.config.LoginBeanRegister;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;
import com.jerocaller.AuthTokenSecurity.data.entity.AuthToken;
import com.jerocaller.AuthTokenSecurity.data.entity.User;
import com.jerocaller.AuthTokenSecurity.data.repository.AuthTokenRepository;
import com.jerocaller.AuthTokenSecurity.data.repository.UserRepository;
import com.jerocaller.AuthTokenSecurity.mockbean.LoginHelper;
import com.jerocaller.AuthTokenSecurity.util.TestUtil;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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
@Slf4j
@Import({LoginBeanRegister.class})
public class RefreshTokenTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginHelper loginHelper;

    private final String REISSUE_URI = "/api/auth/token/reissue";
    private final UserRequest userRequest = UserRequest.builder()
        .username("kimquel123")
        .password("kimquel123")
        .age(23)
        .build();

    @BeforeEach
    void init() throws Exception {
        // 회원가입
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
        log.info("issuer: {}로 테스트 실행.", jwtProperties.getIssuer());

        String jwtAuthProviderName = jwtAuthenticationProvider.getClass().getSimpleName();
        assertThat(jwtAuthProviderName).isEqualTo(
            CustomJwtAuthenticationProvider.class.getSimpleName()
        );
        log.info("테스트에 사용할 JWT auth provider name: {}", jwtAuthProviderName);
    }

    @Test
    @DisplayName("JWT 토큰 재발급 테스트")
    void testAuthTokenReissue() throws Exception {
        AuthTokensDTO prevAuthTokens = loginHelper.login(userRequest);
        assertThat(jwtAuthenticationProvider.validateToken(prevAuthTokens.getAccessToken()))
            .isTrue();
        assertThat(jwtAuthenticationProvider.validateToken(prevAuthTokens.getRefreshToken()))
            .isTrue();

        Optional<AuthToken> optionalPrevAuthToken = authTokenRepository
            .findByRefreshToken(prevAuthTokens.getRefreshToken());
        assertThat(optionalPrevAuthToken.isPresent()).isTrue();

        AuthToken prevAuthTokensEntity = optionalPrevAuthToken.get();
        assertThat(prevAuthTokensEntity.getPreviousRefreshToken()).isNull();
        assertThat(prevAuthTokensEntity.isValid()).isTrue();

        // access token 시간 만료
        TestUtil.delay(jwtProperties.getToken().getAccess().getExpiry());
        Assertions.assertThatExceptionOfType(ExpiredJwtException.class)
                .isThrownBy(() ->
                    jwtAuthenticationProvider.validateToken(prevAuthTokens.getAccessToken())
                );
        assertThat(jwtAuthenticationProvider.validateToken(prevAuthTokens.getRefreshToken()))
            .isTrue();

        final MvcResult reissueResult = mockMvc.perform(post(REISSUE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prevAuthTokens))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andExpect(jsonPath("$.data.refreshToken").exists())
            .andDo(print())
            .andReturn();
        AuthTokensDTO reissuedAuthTokens = TestUtil.extractJwtTokens(reissueResult);
        assertThat(jwtAuthenticationProvider.validateToken(reissuedAuthTokens.getAccessToken()))
            .isTrue();
        assertThat(jwtAuthenticationProvider.validateToken(reissuedAuthTokens.getRefreshToken()))
            .isTrue();
        assertThat(prevAuthTokens.getAccessToken())
            .isNotEqualTo(reissuedAuthTokens.getAccessToken());
        assertThat(prevAuthTokens.getRefreshToken())
            .isNotEqualTo(reissuedAuthTokens.getRefreshToken());

        Optional<AuthToken> optionalReissuedAuthToken = authTokenRepository
            .findByRefreshToken(reissuedAuthTokens.getRefreshToken());
        assertThat(optionalReissuedAuthToken.isPresent()).isTrue();

        AuthToken reissuedAuthTokenEntity = optionalReissuedAuthToken.get();
        assertThat(reissuedAuthTokenEntity.getRefreshToken())
            .isNotEqualTo(prevAuthTokens.getRefreshToken());
        assertThat(reissuedAuthTokenEntity.getPreviousRefreshToken()).isNotNull();
        assertThat(reissuedAuthTokenEntity.getPreviousRefreshToken())
            .isEqualTo(prevAuthTokens.getRefreshToken());
        assertThat(reissuedAuthTokenEntity.isValid()).isTrue();
    }

    @Test
    @DisplayName("""
        만료된 refresh token으로 토큰 재발급 요청 시 새 토큰이 발급되지 않고 재로그인하라는 응답을 받는다.
    """)
    void shouldResponseToReLoginWhenRefreshTokenExpired() throws Exception {
        AuthTokensDTO prevAuthTokens = loginHelper.login(userRequest);
        assertThat(jwtAuthenticationProvider.validateToken(prevAuthTokens.getAccessToken()))
            .isTrue();
        assertThat(jwtAuthenticationProvider.validateToken(prevAuthTokens.getRefreshToken()))
            .isTrue();

        Optional<AuthToken> optionalPrevAuthToken = authTokenRepository
            .findByRefreshToken(prevAuthTokens.getRefreshToken());
        assertThat(optionalPrevAuthToken.isPresent()).isTrue();

        AuthToken prevAuthTokensEntity = optionalPrevAuthToken.get();
        assertThat(prevAuthTokensEntity.getPreviousRefreshToken()).isNull();
        assertThat(prevAuthTokensEntity.isValid()).isTrue();

        // refresh token 시간을 만료시킨다.
        TestUtil.delay(jwtProperties.getToken().getRefresh().getExpiry());
        Assertions.assertThatExceptionOfType(ExpiredJwtException.class)
            .isThrownBy(() ->
                jwtAuthenticationProvider.validateToken(prevAuthTokens.getAccessToken())
            );
        Assertions.assertThatExceptionOfType(ExpiredJwtException.class)
            .isThrownBy(() ->
                jwtAuthenticationProvider.validateToken(prevAuthTokens.getRefreshToken())
            );

        // 토큰 재발급 요청
        ResponseCode expectedCode = ResponseCode.REFRESH_TOKEN_EXPIRED;
        mockMvc.perform(post(REISSUE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(prevAuthTokens))
        )
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(expectedCode.getMessage()))
            .andExpect(jsonPath("$.code").value(expectedCode.getCode()))
            .andExpect(jsonPath("$.data").doesNotExist())
            .andDo(print());

        // DB에서 refresh token 상태에 변화가 없어야 한다.
        Optional<AuthToken> optionalAuthToken = authTokenRepository
            .findByRefreshToken(prevAuthTokens.getRefreshToken());
        assertThat(optionalAuthToken.isPresent()).isTrue();

        AuthToken authTokenEntity = optionalAuthToken.get();
        assertThat(authTokenEntity.getPreviousRefreshToken()).isNull();
        assertThat(authTokenEntity.isValid()).isTrue();
    }

    @Test
    @DisplayName("요청으로 refresh token이 주어지지 않으면 재발급 요청이 거절된다.")
    void shouldRejectWhenRefreshTokenNotGiven() throws Exception {
        // 설령 로그인을 한 상태라도 refresh token을 서버에 넘기지 않으면
        // 토큰이 재발급되지 않음을 테스트.
        AuthTokensDTO prevAuthTokens = loginHelper.login(userRequest);
        assertThat(jwtAuthenticationProvider.validateToken(prevAuthTokens.getAccessToken()))
            .isTrue();
        assertThat(jwtAuthenticationProvider.validateToken(prevAuthTokens.getRefreshToken()))
            .isTrue();

        Optional<AuthToken> optionalPrevAuthToken = authTokenRepository
            .findByRefreshToken(prevAuthTokens.getRefreshToken());
        assertThat(optionalPrevAuthToken.isPresent()).isTrue();

        AuthToken prevAuthTokensEntity = optionalPrevAuthToken.get();
        assertThat(prevAuthTokensEntity.getPreviousRefreshToken()).isNull();
        assertThat(prevAuthTokensEntity.isValid()).isTrue();

        // 비어있는 요청 body
        AuthTokensDTO requestAuthTokens = AuthTokensDTO.builder().build();
        ResponseCode expectedCode = ResponseCode.INVALID_REQUEST_FORMAT;
        mockMvc.perform(post(REISSUE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestAuthTokens))
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(expectedCode.getMessage()))
            .andExpect(jsonPath("$.code").value(expectedCode.getCode()))
            .andExpect(jsonPath("$.data.refreshToken")
                .value("refresh token 값의 null 또는 공백은 비허용됩니다.")
            )
            .andDo(print());

        // DB에서 refresh token 상태에 변화가 없어야 한다.
        Optional<AuthToken> optionalAuthToken = authTokenRepository
            .findByRefreshToken(prevAuthTokens.getRefreshToken());
        assertThat(optionalAuthToken.isPresent()).isTrue();

        AuthToken authTokenEntity = optionalAuthToken.get();
        assertThat(authTokenEntity.getPreviousRefreshToken()).isNull();
        assertThat(authTokenEntity.isValid()).isTrue();
    }

    @Test
    @DisplayName("""
        DB에 등록되지 않은 유저 명의로 refresh token이 요청으로 주어질 때 토큰 재발급이 거절되어야 한다.
    """)
    void shouldRejectWhenFalseRefreshTokenIsGiven() throws Exception {
        // 등록되지 않은 가짜 유저
        User notRegisteredUserEntity = User.builder()
            .id(1)
            .username("notUser123")
            .password("notUser123")
            .age(35)
            .build();
        AuthTokensDTO falseAuthTokens = AuthTokensDTO.builder()
            .refreshToken(jwtAuthenticationProvider
                .createRefreshToken(notRegisteredUserEntity)
            )
            .build();

        // 가짜 유저는 DB 상에 존재하지 않아야 한다.
        Optional<User> optionalUser = userRepository
            .findByUsername(notRegisteredUserEntity.getUsername());
        assertThat(optionalUser.isEmpty()).isTrue();

        Optional<AuthToken> optionalAuthToken = authTokenRepository
            .findByRefreshToken(falseAuthTokens.getRefreshToken());
        assertThat(optionalAuthToken.isEmpty()).isTrue();

        ResponseCode expectedCode = ResponseCode.USER_NOT_FOUND;
        mockMvc.perform(post(REISSUE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(falseAuthTokens))
        )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(expectedCode.getMessage()))
            .andExpect(jsonPath("$.code").value(expectedCode.getCode()))
            .andExpect(jsonPath("$.data").doesNotExist())
            .andDo(print());

        // 토큰 재발급 api 요청 후에도 DB 상에는 가짜 유저 관련 데이터가 없어야 한다.
        Optional<User> optionalUserAfterReq = userRepository
            .findByUsername(notRegisteredUserEntity.getUsername());
        assertThat(optionalUserAfterReq.isEmpty()).isTrue();

        Optional<AuthToken> optionalAuthTokenAfterReq = authTokenRepository
            .findByRefreshToken(falseAuthTokens.getRefreshToken());
        assertThat(optionalAuthTokenAfterReq.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("잘못된 형식의 refresh token이 주어질 경우 토큰 재발급이 거절되어야 한다.")
    void shouldRejectWhenFormatOfRefreshTokenIsWrong() throws Exception {
        // 로그인 상태에서도 잘못된 형식의 refresh token을 서버에 넘길 경우
        // 토큰 재발급이 거절되도록 하고, DB 상에서도 변화가 없음을 테스트.
        AuthTokensDTO prevAuthTokens = loginHelper.login(userRequest);
        assertThat(jwtAuthenticationProvider.validateToken(prevAuthTokens.getAccessToken()))
            .isTrue();
        assertThat(jwtAuthenticationProvider.validateToken(prevAuthTokens.getRefreshToken()))
            .isTrue();
        assertThat(userRepository.existsByUsername(userRequest.getUsername())).isTrue();

        Optional<AuthToken> optionalPrevAuthToken = authTokenRepository
            .findByRefreshToken(prevAuthTokens.getRefreshToken());
        assertThat(optionalPrevAuthToken.isPresent()).isTrue();

        AuthToken prevAuthTokensEntity = optionalPrevAuthToken.get();
        assertThat(prevAuthTokensEntity.getPreviousRefreshToken()).isNull();
        assertThat(prevAuthTokensEntity.isValid()).isTrue();

        AuthTokensDTO falseAuthTokens = AuthTokensDTO.builder()
            .refreshToken("ThisIs.NotA.RefreshToken")
            .build();
        ResponseCode expectedCode = ResponseCode.JWT_UNEXPECTED_FORMAT;
        mockMvc.perform(post(REISSUE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(falseAuthTokens))
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(expectedCode.getMessage()))
            .andExpect(jsonPath("$.code").value(expectedCode.getCode()))
            .andExpect(jsonPath("$.data").doesNotExist())
            .andDo(print());

        // 토큰 재발급 api 요청 후에도 DB 상에서는 아무런 변화가 없어야 한다.
        assertThat(userRepository.existsByUsername(userRequest.getUsername())).isTrue();
        assertThat(userRepository.findAll().size()).isOne();
        assertThat(authTokenRepository.findAll().size()).isOne();
        assertThat(authTokenRepository.findByRefreshToken(falseAuthTokens.getRefreshToken())
            .isEmpty()).isTrue();
        assertThat(authTokenRepository.findByPreviousRefreshToken(falseAuthTokens.getRefreshToken())
            .isEmpty()).isTrue();

        Optional<AuthToken> optionalUserAuthToken = authTokenRepository
            .findByRefreshToken(prevAuthTokens.getRefreshToken());
        assertThat(optionalUserAuthToken.isPresent()).isTrue();

        AuthToken userAuthTokenEntity = optionalUserAuthToken.get();
        assertThat(userAuthTokenEntity.getPreviousRefreshToken()).isNull();
        assertThat(userAuthTokenEntity.isValid()).isTrue();
        assertThat(userAuthTokenEntity.getUser()).isNotNull();
        assertThat(userAuthTokenEntity.getUser().getUsername())
            .isEqualTo(userRequest.getUsername());
    }
}
