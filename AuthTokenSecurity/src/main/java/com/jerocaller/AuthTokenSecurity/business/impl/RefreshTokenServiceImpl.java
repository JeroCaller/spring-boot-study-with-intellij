package com.jerocaller.AuthTokenSecurity.business.impl;

import com.jerocaller.AuthTokenSecurity.business.RefreshTokenService;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.entity.AuthToken;
import com.jerocaller.AuthTokenSecurity.data.entity.User;
import com.jerocaller.AuthTokenSecurity.data.repository.AuthTokenRepository;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtAuthenticationProvider;
import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final AuthTokenRepository authTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public AuthTokensDTO generateNewRefreshToken(UserDetails userDetails) {
        String newRefreshToken = jwtAuthenticationProvider.createRefreshToken(userDetails);
        log.info("refresh token 길이: {}", newRefreshToken.length());
        Optional<AuthToken> optAuthTokenEntity = authTokenRepository.findByUser(userDetails);

        if (optAuthTokenEntity.isPresent()) {
            AuthToken authToken = optAuthTokenEntity.get();
            authToken.setPreviousRefreshToken(authToken.getRefreshToken());
            authToken.setRefreshToken(newRefreshToken);
        } else {
            AuthToken newAuthTokenEntity = AuthToken.builder()
                .user((User) userDetails)
                .refreshToken(newRefreshToken)
                .isValid(true)
                .build();
            authTokenRepository.save(newAuthTokenEntity);
        }

        return AuthTokensDTO.builder()
            .refreshToken(newRefreshToken)
            .build();
    }

    @Override
    @Transactional
    public void invalidateRefreshToken(HttpServletRequest httpRequest) {
        Cookie refreshTokenCookie =  WebUtils.getCookie(httpRequest,
            jwtProperties.getToken().getRefresh().getCookieName());

        if (refreshTokenCookie == null) {
            return;
        }

        Optional<AuthToken> optAuthToken = authTokenRepository
            .findByRefreshToken(refreshTokenCookie.getValue());

        if (optAuthToken.isEmpty()) {
            return;
        }

        AuthToken foundAuthToken = optAuthToken.get();
        foundAuthToken.setPreviousRefreshToken(foundAuthToken.getRefreshToken());
        foundAuthToken.setValid(false);
        foundAuthToken.setRefreshToken(null);
    }
}
