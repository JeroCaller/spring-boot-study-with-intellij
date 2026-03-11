package com.jerocaller.AuthTokenSecurity.data.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateResponse {
    private UserResponse userResponse;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuthTokensDTO authTokensDTO;
}
