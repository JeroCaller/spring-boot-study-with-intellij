package com.jerocaller.AuthTokenSecurity.data.repository;

import com.jerocaller.AuthTokenSecurity.data.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {
    Optional<AuthToken> findByUser(UserDetails userDetails);
    Optional<AuthToken> findByRefreshToken(String refreshToken);
    Optional<AuthToken> findByPreviousRefreshToken(String refreshToken);
    boolean existsByRefreshToken(String refreshToken);
    boolean existsByPreviousRefreshToken(String refreshToken);
}
