package com.jerocaller.AuthTokenSecurity.controller;

import com.jerocaller.AuthTokenSecurity.business.AuthService;
import com.jerocaller.AuthTokenSecurity.business.AuthTokenService;
import com.jerocaller.AuthTokenSecurity.business.RefreshTokenService;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.dto.request.AuthRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.RestResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthTokenController {
    private final AuthService authService;
    private final AuthTokenService authTokenService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<RestResponse.DetailedRestResponse<AuthTokensDTO>> login(
        @RequestBody AuthRequest authRequest
    ) {
        AuthTokensDTO authTokensDTO = authService.login(authRequest);
        return RestResponse.success(authTokensDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<RestResponse.DetailedRestResponse<Object>> logout(
        HttpServletRequest httpRequest,
        HttpServletResponse httpResponse
    ) {
        authService.logout(httpRequest, httpResponse);
        return RestResponse.success(null);
    }

    /**
     * <p>액세스 토큰 만료 시 리프레시 토큰과 함께 서버에 요청하여 인증 토큰을 재발급한다.</p>
     * <p>재발급 조건 - 사용자가 별도로 재로그인을 하지 않아도 됨.</p>
     * <ul>
     *     <li>
     *         인증된 사용자의 액세스 토큰이 만료되었고, 만료되지 않은 리프레시 토큰과 함께
     *         서버에 요청한 경우.
     *     </li>
     * </ul>
     * <p>재발급 불가한 상황 - 사용자의 재로그인 필요</p>
     * <ul>
     *     <li>
     *         서버에 전달한 현재 리프레시 토큰도 시간이 지나 만료된 경우
     *     </li>
     *     <li>
     *         이미 이전에 만료되어 새로운 리프레시 토큰이 발급된 상황에서,
     *         이전의 만료된 리프레시 토큰을 서버에 전달할 경우. -> TRD(Token Reuse Detection)의 대상.
     *     </li>
     * </ul>
     *
     * @param requestedAuthTokensDTO 만료된 액세스 토큰 및 리프레시 토큰
     * @return
     */
    @PostMapping("/token/reissue")
    public ResponseEntity<RestResponse.DetailedRestResponse<AuthTokensDTO>> reissueToken(
        @Valid @RequestBody AuthTokensDTO requestedAuthTokensDTO
    ) {
        refreshTokenService.detectTokenReuse(requestedAuthTokensDTO);
        AuthTokensDTO authTokensToResponse = authTokenService
            .reissueAuthTokens(requestedAuthTokensDTO);
        return RestResponse.success(authTokensToResponse);
    }
}
