package com.jerocaller.AuthTokenSecurity.business.impl;

import com.jerocaller.AuthTokenSecurity.business.AuthTokenService;
import com.jerocaller.AuthTokenSecurity.business.UserService;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserInfoPatchRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.UserResponse;
import com.jerocaller.AuthTokenSecurity.data.dto.response.UserUpdateResponse;
import com.jerocaller.AuthTokenSecurity.data.entity.AuthToken;
import com.jerocaller.AuthTokenSecurity.data.entity.User;
import com.jerocaller.AuthTokenSecurity.data.repository.AuthTokenRepository;
import com.jerocaller.AuthTokenSecurity.data.repository.UserRepository;
import com.jerocaller.AuthTokenSecurity.exception.custom.UsernameAlreadyExistsException;
import com.jerocaller.AuthTokenSecurity.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDetailsService userDetailsService;
    private final AuthTokenService authTokenService;
    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;
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
    public UserUpdateResponse updateUserInfo(UserInfoPatchRequest patchRequest) {
        String currentUsername = AuthUtil.getAuth();
        log.info("current username: {}", currentUsername);
        User currentUser = (User) userDetailsService.loadUserByUsername(currentUsername);
        boolean willUsernameBeChanged = (patchRequest.getUsername() != null) &&
            !currentUser.getUsername().equals(patchRequest.getUsername());

        // 다른 유저의 유저네임으로 변경 방지
        if (willUsernameBeChanged && userRepository.existsByUsername(patchRequest.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        currentUser.update(patchRequest, passwordEncoder);
        UserResponse userResponse = UserResponse.toDto(currentUser);
        AuthTokensDTO newAuthTokensDto = null;

        // username을 기반으로 인증 토큰이 발급되는 방식이고,
        // 서버에서 인증 토큰을 통해 사용자를 식별하는 방법이 username을 이용한 방법이므로
        // username 변경 시 새 인증 토큰으로 변경해야함.
        if (willUsernameBeChanged) {
            newAuthTokensDto = authTokenService.generateAuthTokens(currentUser);
        }

        return UserUpdateResponse.builder()
            .userResponse(userResponse)
            .authTokensDTO(newAuthTokensDto)
            .build();
    }

    @Override
    @Transactional
    public UserResponse unregister() {
        String username = AuthUtil.getAuth();
        UserDetails currentUserDetails = userDetailsService.loadUserByUsername(username);
        Optional<AuthToken> optAuthToken = authTokenRepository.findByUser(currentUserDetails);
        optAuthToken.ifPresent(authTokenRepository::delete);
        userRepository.delete((User) currentUserDetails);
        return UserResponse.toDto((User) currentUserDetails);
    }
}
