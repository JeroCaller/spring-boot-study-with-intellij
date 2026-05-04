package com.jerocaller.AuthTokenSecurity.business;

import com.jerocaller.AuthTokenSecurity.data.dto.response.UserAuthStatusResponse;

/**
 * <p>
 *     현재 사용자의 인증 상태를 확인하는 서비스.
 * </p>
 */
public interface UserAuthStatusService {
    default UserAuthStatusResponse getUserAuthStatus() { return null; };
    default UserAuthStatusResponse getUserAuthStatus(String username) { return null; };
}
