package com.jerocaller.AuthTokenSecurity.business.impl;

import com.jerocaller.AuthTokenSecurity.business.AuthTokenService;
import com.jerocaller.AuthTokenSecurity.business.RefreshTokenService;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    @Override
    public AuthTokensDTO generateAuthTokens(UserDetails userDetails) {
        return _generateAuthTokens(userDetails);
    }

    @Override
    public AuthTokensDTO reissueAuthTokens(AuthTokensDTO oldAuthTokensDto) {
        String username = jwtAuthenticationProvider
            .extractUsernameFromToken(oldAuthTokensDto.getAccessToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return _generateAuthTokens(userDetails);
    }

    private AuthTokensDTO _generateAuthTokens(UserDetails userDetails) {
        String accessToken = jwtAuthenticationProvider.createAccessToken(userDetails);
        AuthTokensDTO authTokensDTO = refreshTokenService.generateNewRefreshToken(userDetails);
        authTokensDTO.setAccessToken(accessToken);
        return authTokensDTO;
    }
}
