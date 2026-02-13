package com.jerocaller.AuthTokenSecurity.business;

import com.jerocaller.AuthTokenSecurity.data.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface RefreshTokenService {
    <R> R generateNewRefreshToken(User user);
    void invalidateRefreshToken(HttpServletRequest httpRequest);
}
