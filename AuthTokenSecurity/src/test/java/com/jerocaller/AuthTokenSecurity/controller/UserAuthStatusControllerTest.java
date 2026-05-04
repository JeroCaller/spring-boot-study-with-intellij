package com.jerocaller.AuthTokenSecurity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerocaller.AuthTokenSecurity.common.UserRoles;
import com.jerocaller.AuthTokenSecurity.config.LoginBeanRegister;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.util.LoginHelper;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>테스트 방법</p>
 * <ul>
 *     <li>
 *         <code>UserAuthStatusController</code>를 대상으로 테스트하며,
 *         <code>UserAuthStatusService</code>를 구현하는 구현체들 중 테스트 대상 service bean에
 *         {@code @Primary}를 부여한다.
 *     </li>
 *     <li>
 *         해당 service 구현체에 알맞는 URI로 변경하여 테스트를 진행한다.
 *     </li>
 * </ul>
 */
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({LoginBeanRegister.class})
class UserAuthStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    // 아래 URI 중 테스트하고자 하는 URI를 선택.
    // 여기에 있는 모든 테스트가 통과되도록 임시로 설정하고자 한다면 PRE_AUTH_URI는 이용하지 말 것.
    private final String SECURITY_CONTEXT_URI = "/api/users/auth/status/security/context";
    private final String PRINCIPAL_ANNO_URI = "/api/users/auth/status/annotation/principal";
    private final String PRE_AUTH_URI = "/api/users/auth/status//annotation/preauth";

    private String getTestURI() {
        return SECURITY_CONTEXT_URI;
    }

    @Test
    @DisplayName("미인증자 정보 테스트.")
    void testAnonymousUser() throws Exception {
        // 인증되지 않은 익명의 유저를 대상으로도 `Authentication.isAuthenticated()` 의 반환값이 true로 나온다.
        mockMvc.perform(get(getTestURI()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.username").value("anonymousUser"))
            .andExpect(jsonPath("$.data.roleNames[0]").value("ROLE_ANONYMOUS"))
            .andExpect(jsonPath("$.data.authenticated").value(true))
            .andDo(print());
    }

    @Test
    @DisplayName("인증자 정보 테스트.")
    void testAuthenticatedUser() throws Exception {
        UserRequest userRequest = UserRequest.builder()
            .username("kimquel123")
            .password("kimquel123")
            .age(23)
            .build();

        // 회원가입
        registerNewUser(userRequest);

        // 로그인
        AuthTokensDTO authTokensDTO = loginHelper.login(userRequest);

        mockMvc.perform(get(getTestURI())
            .header(JwtAuthenticationProvider.AUTHORIZATION,
                JwtAuthenticationProvider.BEARER + authTokensDTO.getAccessToken())
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.username").value(userRequest.getUsername()))
            .andExpect(jsonPath("$.data.roleNames[0]").value(UserRoles.USER.getRoleName()))
            .andExpect(jsonPath("$.data.authenticated").value(true))
            .andDo(print());
    }

    private void registerNewUser(UserRequest userRequest) throws Exception {
        mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)));
    }
}