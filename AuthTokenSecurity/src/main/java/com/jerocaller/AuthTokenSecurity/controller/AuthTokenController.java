package com.jerocaller.AuthTokenSecurity.controller;

import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.RestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthTokenController {

    @PostMapping("/login")
    public RestResponse login(@Valid @RequestBody UserRequest userRequest) {
        return null;
    }

    @PostMapping("/logout")
    public RestResponse logout() {
        return null;
    }

    @PostMapping("/token/reissue")
    public RestResponse reissueToken() {
        return null;
    }
}
