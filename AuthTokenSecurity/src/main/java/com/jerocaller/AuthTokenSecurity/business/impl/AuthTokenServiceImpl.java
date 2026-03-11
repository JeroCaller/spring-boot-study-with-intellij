package com.jerocaller.AuthTokenSecurity.business.impl;

import com.jerocaller.AuthTokenSecurity.business.AuthTokenService;
import com.jerocaller.AuthTokenSecurity.business.RefreshTokenService;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.exception.custom.jwt.JwtUnexpectedFormatException;
import com.jerocaller.AuthTokenSecurity.exception.custom.jwt.RefreshTokenExpiredException;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
        String username;

        // refresh token 만료 시 사용자가 재로그인을 해야함.
        try {
            username = jwtAuthenticationProvider
                .extractUsernameFromToken(oldAuthTokensDto.getRefreshToken());
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        } catch (JwtException e) {
            throw new JwtUnexpectedFormatException();
        }

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
