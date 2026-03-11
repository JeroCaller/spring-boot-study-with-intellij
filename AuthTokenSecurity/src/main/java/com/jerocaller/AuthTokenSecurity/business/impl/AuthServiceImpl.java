package com.jerocaller.AuthTokenSecurity.business.impl;

import com.jerocaller.AuthTokenSecurity.business.AuthService;
import com.jerocaller.AuthTokenSecurity.business.AuthTokenService;
import com.jerocaller.AuthTokenSecurity.business.RefreshTokenService;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.dto.request.AuthRequest;
import com.jerocaller.AuthTokenSecurity.data.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final AuthTokenService authTokenService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextLogoutHandler logoutHandler;

    @Override
    @Transactional
    public AuthTokensDTO login(AuthRequest authRequest) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("인증 실패");
        }

        // 현재 시각을 최근 로그인 시각으로 저장.
        User currentUser = (User) userDetails;
        currentUser.setLastLoginAt(LocalDateTime.now());

        return authTokenService.generateAuthTokens(userDetails);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        refreshTokenService.invalidateRefreshToken(request);
        logoutHandler.logout(
            request,
            response,
            SecurityContextHolder.getContext().getAuthentication()
        );
    }
}
