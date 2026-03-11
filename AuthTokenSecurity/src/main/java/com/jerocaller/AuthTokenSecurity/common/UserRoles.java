package com.jerocaller.AuthTokenSecurity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {
    USER("USER");

    private final String roleName;
}
