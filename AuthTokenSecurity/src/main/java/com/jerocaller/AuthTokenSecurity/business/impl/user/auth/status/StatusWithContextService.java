package com.jerocaller.AuthTokenSecurity.business.impl.user.auth.status;

import com.jerocaller.AuthTokenSecurity.business.UserAuthStatusService;
import com.jerocaller.AuthTokenSecurity.data.dto.response.UserAuthStatusResponse;
import com.jerocaller.AuthTokenSecurity.util.AuthUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     SecurityContextHolder를 이용한 인증 정보 읽기 방식.
 * </p>
 */
@Primary
@Component
public class StatusWithContextService implements UserAuthStatusService {

    @Override
    public UserAuthStatusResponse getUserAuthStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UserAuthStatusResponse.builder()
            .username(authentication.getName())
            .roleNames(AuthUtil.getRoleNames(authentication))
            .isAuthenticated(authentication.isAuthenticated())  // 익명 여부 상관없이 true
            //.isAuthenticated(!(authentication instanceof AnonymousAuthenticationToken)) // 익명: false
            //.isAuthenticated(!authentication.getName().equals("anonymousUser")) // 익명: false
            .build();
    }
}
