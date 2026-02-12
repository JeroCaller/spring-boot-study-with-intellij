package com.jerocaller.AuthTokenSecurity.data.repository;

import com.jerocaller.AuthTokenSecurity.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
