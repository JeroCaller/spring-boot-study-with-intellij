package com.jerocaller.AuthTokenSecurity.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthTokensDTO {
    private String accessToken;
    
    @NotBlank(message = "refresh token 값의 null 또는 공백은 비허용됩니다.")
    private String refreshToken;
}
