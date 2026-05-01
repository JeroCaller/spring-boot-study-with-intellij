package com.jerocaller.AuthTokenSecurity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerocaller.AuthTokenSecurity.config.LoginBeanRegister;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.dto.request.AuthRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserInfoPatchRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;
import com.jerocaller.AuthTokenSecurity.data.entity.User;
import com.jerocaller.AuthTokenSecurity.data.repository.UserRepository;
import com.jerocaller.AuthTokenSecurity.util.LoginHelper;
import com.jerocaller.AuthTokenSecurity.util.TestUtil;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>참고 사항</p>
 * <ul>
 *     <li>
 *         <a href="https://docs.spring.io/spring-framework/reference/testing/testcontext-framework/tx.html">
 *             Spring - Transactional Management
 *         </a>
 *     </li>
 * </ul>
 */
@SpringBootTest
@Transactional  // 테스트 케이스 실행 때마다 테스트 DB가 롤백된다.
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({LoginBeanRegister.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String USER_REQUEST_URI = "/api/users";

    @Test
    @DisplayName("회원가입 성공 여부 테스트.")
    void testUserRegister() throws Exception {
        UserRequest userRequest = UserRequest.builder()
            .username("gugudan123")
            .password("gugudan123")
            .age(30)
            .build();

        mockMvc.perform(post(USER_REQUEST_URI)
            .content(objectMapper.writeValueAsString(userRequest))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.httpStatus")
                .value(ResponseCode.OK.getHttpStatus().getReasonPhrase())
            )
            .andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.username").value(userRequest.getUsername()))
            .andDo(print());

        assertThat(userRepository.existsByUsername(userRequest.getUsername())).isTrue();
    }

    @Test
    @DisplayName("회원가입 시 특정 필드가 조건을 미충족 시 회원가입이 거절된다.")
    void testInvalidUserRegister() throws Exception {
        UserRequest userRequest = UserRequest.builder()
            .username("")  // invalid format
            .password("12345")
            .age(50)
            .build();

        mockMvc.perform(post(USER_REQUEST_URI)
                .content(objectMapper.writeValueAsString(userRequest))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message")
                .value(ResponseCode.INVALID_REQUEST_FORMAT.getMessage())
            )
            .andExpect(jsonPath("$.code")
                .value(ResponseCode.INVALID_REQUEST_FORMAT.getCode())
            )
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.username").exists())
            .andExpect(jsonPath("$.data.password").doesNotExist())
            .andExpect(jsonPath("$.data.age").doesNotExist())
            .andDo(print());

        assertThat(userRepository.existsByUsername(userRequest.getUsername())).isFalse();
    }

    @Test
    @DisplayName("회원가입 시 기존에 등록된 username으로 회원가입 시도 시 회원가입이 되지 않아야한다.")
    void testCantRegisterIfUsernameAlreadyExists() throws Exception {
        UserRequest memberRequest = UserRequest.builder()
            .username("gugudan123")
            .password("gugudan123")
            .age(30)
            .build();

        // 회원 가입
        mockMvc.perform(post(USER_REQUEST_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(memberRequest))
        )
            .andExpect(status().isOk());
        assertThat(userRepository.existsByUsername(memberRequest.getUsername())).isTrue();

        // 기존 회원의 username과 겹치는 새 회원가입 시도.
        UserRequest newUserRequest = UserRequest.builder()
            .username(memberRequest.getUsername())
            .password("wow123")
            .age(20)
            .build();

        ResponseCode expectedCode = ResponseCode.USERNAME_ALREADY_EXISTS;
        mockMvc.perform(post(USER_REQUEST_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newUserRequest))
        )
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code")
                .value(expectedCode.getCode())
            )
            .andExpect(jsonPath("$.message")
                .value(expectedCode.getMessage())
            )
            .andDo(print());

        Optional<User> optUser = userRepository.findByUsername(newUserRequest.getUsername());
        assertThat(optUser.isPresent()).isTrue();

        User member = optUser.get();
        assertThat(member.getAge()).isNotEqualTo(newUserRequest.getAge());
    }

    @Test
    @DisplayName("로그인 후 보호된 자원에 접근 가능해야한다.")
    void shouldBeAbleToAccessToProtectedResourceAfterLogin() throws Exception {
        UserRequest userRequest = UserRequest.builder()
            .username("gugudan123")
            .password("gugudan123")
            .age(30)
            .build();

        // 회원가입
        mockMvc.perform(post(USER_REQUEST_URI)
            .content(objectMapper.writeValueAsString(userRequest))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andDo(print());

        // 로그인
        AuthRequest authRequest = AuthRequest.builder()
            .username(userRequest.getUsername())
            .password(userRequest.getPassword())
            .build();
        final MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
            .content(objectMapper.writeValueAsString(authRequest))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andExpect(jsonPath("$.data.refreshToken").exists())
            .andDo(print())
            .andReturn();
        AuthTokensDTO responseAuthTokens = TestUtil.extractJwtTokens(loginResult);

        // 보호된 자원 요청
        mockMvc.perform(get(USER_REQUEST_URI + "/{username}", userRequest.getUsername())
                .header(
                    JwtAuthenticationProvider.AUTHORIZATION,
                    JwtAuthenticationProvider.BEARER + responseAuthTokens.getAccessToken()
                )
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.username").value(userRequest.getUsername()))
            .andExpect(jsonPath("$.data.age").value(userRequest.getAge()))
            .andExpect(jsonPath("$.data.lastLoginAt").exists())
            .andDo(print());
    }

    @Test
    @DisplayName("비로그인 사용자는 보호된 자원에 접근 불가능해야한다.")
    void shouldNotAbleToAccessToProtectedResourceIfNotLogin() throws Exception {
        UserRequest userRequest = UserRequest.builder()
            .username("gugudan123")
            .password("gugudan123")
            .age(30)
            .build();

        // 회원가입
        mockMvc.perform(post(USER_REQUEST_URI)
                .content(objectMapper.writeValueAsString(userRequest))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(print());

        // 보호된 자원 요청
        mockMvc.perform(get(USER_REQUEST_URI + "/{username}", userRequest.getUsername()))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message")
                .value(ResponseCode.AUTHENTICATION_FAILED.getMessage())
            )
            .andExpect(jsonPath("$.code")
                .value(ResponseCode.AUTHENTICATION_FAILED.getCode())
            )
            .andExpect(jsonPath("$.data").doesNotExist())
            .andDo(print());
    }

    @Test
    @DisplayName("유저 정보 업데이트 요청 후 유저 정보가 영구적으로 변경되어야 한다.")
    void shouldUpdateUserInfo() throws Exception {
        UserRequest memberRequest = UserRequest.builder()
            .username("gugudan123")
            .password("gugudan123")
            .age(30)
            .build();

        // 회원 가입
        mockMvc.perform(post(USER_REQUEST_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequest))
            )
            .andExpect(status().isOk());
        assertThat(userRepository.existsByUsername(memberRequest.getUsername())).isTrue();

        // 로그인
        AuthTokensDTO authTokensDTO = loginHelper.login(memberRequest);

        // 유저 정보 업데이트 요청
        UserInfoPatchRequest userInfoPatchRequest = UserInfoPatchRequest.builder()
            .username("kimquel123")
            .age(memberRequest.getAge() + 10)
            .build();

        mockMvc.perform(patch(USER_REQUEST_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userInfoPatchRequest))
            .header(
                JwtAuthenticationProvider.AUTHORIZATION,
                JwtAuthenticationProvider.BEARER + authTokensDTO.getAccessToken()
            )
        )
            .andExpect(status().isOk())
            .andDo(print());

        assertThat(userRepository.existsByUsername(memberRequest.getUsername())).isFalse();

        Optional<User> foundUserOpt = userRepository
            .findByUsername(userInfoPatchRequest.getUsername());
        assertThat(foundUserOpt.isPresent()).isTrue();

        User foundUser = foundUserOpt.get();
        assertThat(foundUser.getUsername()).isNotEqualTo(memberRequest.getUsername());
        assertThat(passwordEncoder.matches(memberRequest.getPassword(), foundUser.getPassword()))
            .isTrue();
        assertThat(foundUser.getAge()).isNotEqualTo(memberRequest.getAge());
        assertThat(foundUser.getAge()).isEqualTo(memberRequest.getAge() + 10);
    }
}