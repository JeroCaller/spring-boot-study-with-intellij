package com.jerocaller.AuthTokenSecurity.controller;

import com.jerocaller.AuthTokenSecurity.business.AuthService;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.dto.request.AuthRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.RestResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @PostMapping("/token/reissue")
    public RestResponse reissueToken() {
        return null;
    }
}
