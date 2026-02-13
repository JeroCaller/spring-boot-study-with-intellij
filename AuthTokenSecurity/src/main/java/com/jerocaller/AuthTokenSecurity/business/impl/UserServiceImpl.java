package com.jerocaller.AuthTokenSecurity.business.impl;

import com.jerocaller.AuthTokenSecurity.business.UserService;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserInfoPatchRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.UserResponse;
import com.jerocaller.AuthTokenSecurity.data.entity.User;
import com.jerocaller.AuthTokenSecurity.data.repository.UserRepository;
import com.jerocaller.AuthTokenSecurity.exception.custom.UsernameAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUserInfo(String username) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        return UserResponse.toDto(user);
    }

    @Override
    @Transactional
    public UserResponse register(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        User newUser = userRepository.save(User.toEntity(userRequest, passwordEncoder));
        return UserResponse.toDto(newUser);
    }

    @Override
    public <R> R updateUserInfo(UserInfoPatchRequest patchRequest) {
        return null;
    }

    @Override
    public <R> R unregister() {
        return null;
    }
}
