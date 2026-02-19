package com.jerocaller.AuthTokenSecurity.business;

import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface RefreshTokenService {
    AuthTokensDTO generateNewRefreshToken(UserDetails userDetails);
    AuthTokensDTO reissueRefreshToken(AuthTokensDTO authTokensDTO);
    void invalidateRefreshToken(HttpServletRequest httpRequest);
}
