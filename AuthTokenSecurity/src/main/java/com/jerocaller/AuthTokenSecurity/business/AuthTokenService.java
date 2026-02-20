package com.jerocaller.AuthTokenSecurity.business;

import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthTokenService {
    AuthTokensDTO generateAuthTokens(UserDetails userDetails);
    AuthTokensDTO reissueAuthTokens(AuthTokensDTO oldAuthTokensDto);
}
