package com.jerocaller.AuthTokenSecurity.data.repository;

import com.jerocaller.AuthTokenSecurity.data.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {
}
