package com.jerocaller.AuthTokenSecurity.business.impl.user.auth.status;

import com.jerocaller.AuthTokenSecurity.business.UserAuthStatusService;
import com.jerocaller.AuthTokenSecurity.data.dto.response.UserAuthStatusResponse;
import com.jerocaller.AuthTokenSecurity.util.AuthUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     {@code @AuthenticationPrincipal} 어노테이션을 이용하여 인증 정보를 가져오는 방식.
 * </p>
 */
// @Primary
@Component
public class StatusWithAuthAnnoService implements UserAuthStatusService {

    @Override
    public UserAuthStatusResponse getUserAuthStatus(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UserAuthStatusResponse.builder()
            .username(username)
            .roleNames(AuthUtil.getRoleNames(authentication))
            .isAuthenticated(authentication.isAuthenticated())
            .build();
    }
}
