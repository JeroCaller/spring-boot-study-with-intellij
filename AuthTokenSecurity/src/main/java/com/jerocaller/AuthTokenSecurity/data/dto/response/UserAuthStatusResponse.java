package com.jerocaller.AuthTokenSecurity.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthStatusResponse {
    private String username;
    private List<String> roleNames;
    private boolean isAuthenticated;
}
