package com.jerocaller.AuthTokenSecurity.business;

import com.jerocaller.AuthTokenSecurity.data.dto.request.UserInfoPatchRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;

public interface UserService {
    <R> R getUserInfo(String username);
    <R> R register(UserRequest userRequest);
    <R> R updateUserInfo(UserInfoPatchRequest patchRequest);
    <R> R unregister();
}
