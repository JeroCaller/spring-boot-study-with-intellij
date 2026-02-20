package com.jerocaller.AuthTokenSecurity.business;

import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.dto.request.AuthRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthTokensDTO login(AuthRequest authRequest);
    void logout(HttpServletRequest request, HttpServletResponse response);
}
