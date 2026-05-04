package com.jerocaller.AuthTokenSecurity.util;

import com.jerocaller.AuthTokenSecurity.common.UserRoles;
import com.jerocaller.AuthTokenSecurity.exception.custom.NotAuthenticatedUserException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class AuthUtil {
    public static String getAuth() {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
            || isAnonymous(authentication)
        ) {
            throw new NotAuthenticatedUserException();
        }

        return (String) authentication.getPrincipal();
    }

    public static boolean isAnonymous(Authentication authentication) {
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals(UserRoles.USER.getRoleName())) {
                return false;
            }
        }

        return true;
    }

    public static List<String> getRoleNames(Authentication authentication) {
        List<String> roleNames = new ArrayList<>();

        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            roleNames.add(grantedAuthority.getAuthority());
        }

        return roleNames;
    }
}
