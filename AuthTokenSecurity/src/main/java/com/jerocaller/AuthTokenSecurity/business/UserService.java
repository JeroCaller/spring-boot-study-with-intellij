package com.jerocaller.AuthTokenSecurity.business;

import com.jerocaller.AuthTokenSecurity.data.dto.request.UserInfoPatchRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.UserResponse;
import com.jerocaller.AuthTokenSecurity.data.dto.response.UserUpdateResponse;

public interface UserService {
    UserResponse getUserInfo(String username);
    UserResponse register(UserRequest userRequest);
    UserUpdateResponse updateUserInfo(UserInfoPatchRequest patchRequest);
    UserResponse unregister();
}
