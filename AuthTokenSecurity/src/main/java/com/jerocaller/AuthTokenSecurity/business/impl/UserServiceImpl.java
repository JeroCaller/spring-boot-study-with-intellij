package com.jerocaller.AuthTokenSecurity.business.impl;

import com.jerocaller.AuthTokenSecurity.business.UserService;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserInfoPatchRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.UserResponse;
import com.jerocaller.AuthTokenSecurity.data.entity.User;
import com.jerocaller.AuthTokenSecurity.data.repository.UserRepository;
import com.jerocaller.AuthTokenSecurity.exception.custom.UsernameAlreadyExistsException;
import com.jerocaller.AuthTokenSecurity.util.AuthUtil;
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
    @Transactional
    public UserResponse updateUserInfo(UserInfoPatchRequest patchRequest) {
        String currentUsername = AuthUtil.getAuth();
        User currentUser = (User) userDetailsService.loadUserByUsername(currentUsername);

        // 다른 유저의 유저네임으로 변경 방지
        if (!currentUser.getUsername().equals(patchRequest.getUsername()) &&
            userRepository.existsByUsername(patchRequest.getUsername())
        ) {
            throw new UsernameAlreadyExistsException();
        }

        currentUser.update(patchRequest, passwordEncoder);

        // TODO - username이 변경된 경우 jwt에도 반영되어야 함.
        return UserResponse.toDto(currentUser);
    }

    @Override
    public <R> R unregister() {
        return null;
    }
}
