package com.jerocaller.AuthTokenSecurity.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthTokens {
    private String accessToken;
    private String refreshToken;
}
