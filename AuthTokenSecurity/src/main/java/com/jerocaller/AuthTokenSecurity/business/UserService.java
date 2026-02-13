package com.jerocaller.AuthTokenSecurity.business;

import com.jerocaller.AuthTokenSecurity.data.dto.request.UserInfoPatchRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.UserResponse;

public interface UserService {
    UserResponse getUserInfo(String username);
    UserResponse register(UserRequest userRequest);
    <R> R updateUserInfo(UserInfoPatchRequest patchRequest);
    <R> R unregister();
}
