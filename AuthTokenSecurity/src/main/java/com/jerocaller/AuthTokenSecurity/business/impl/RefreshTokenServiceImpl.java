package com.jerocaller.AuthTokenSecurity.business.impl;

import com.jerocaller.AuthTokenSecurity.business.RefreshTokenService;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.entity.AuthToken;
import com.jerocaller.AuthTokenSecurity.data.entity.User;
import com.jerocaller.AuthTokenSecurity.data.repository.AuthTokenRepository;
import com.jerocaller.AuthTokenSecurity.exception.custom.ReLoginRequiredException;
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
            // TODO - is_valid = true로 하는 코드 추가.
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

    @Override
    @Transactional
    public void detectTokenReuse(AuthTokensDTO oldAuthTokensDto) {
        if (!jwtAuthenticationProvider.validateToken(oldAuthTokensDto.getRefreshToken())) {
            return;
        }

        if (authTokenRepository.existsByRefreshToken(oldAuthTokensDto.getRefreshToken())) {
            return;
        }

        Optional<AuthToken> optAuthToken = authTokenRepository
            .findByPreviousRefreshToken(oldAuthTokensDto.getRefreshToken());

        if (optAuthToken.isEmpty()) {
            return;
        }

        AuthToken foundAuthToken = optAuthToken.get();
        warnTokenReuseToServer(foundAuthToken);
        foundAuthToken.setValid(false);
        throw new ReLoginRequiredException();
    }

    private void warnTokenReuseToServer(AuthToken foundAuthToken) {
        log.warn("Refresh token 재사용이 감지되었습니다.");
        log.warn("=== 재사용된 refresh token 관련 정보 ===");
        log.warn("auth token id : {}", foundAuthToken.getId());
        log.warn("was the refresh token valid?: {}", foundAuthToken.isValid());
        log.warn("user id: {}", foundAuthToken.getUser().getId());
        log.warn("username: {}", foundAuthToken.getUser().getUsername());
        log.warn("=====");
    }
}
