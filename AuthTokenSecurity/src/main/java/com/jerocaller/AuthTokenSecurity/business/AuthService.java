package com.jerocaller.AuthTokenSecurity.business;

import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokens;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;

public interface AuthService {
    <R> R login(UserRequest userRequest);
    <R> R logout();
    <R> R reissueTokens(AuthTokens authTokens);
}
