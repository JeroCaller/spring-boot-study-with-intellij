package com.jerocaller.AuthTokenSecurity.util;

import com.jayway.jsonpath.JsonPath;
import com.jerocaller.AuthTokenSecurity.data.dto.AuthTokensDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class TestUtil {
    public static void delay(Duration duration) throws InterruptedException {
        log.info("테스트를 위해 {}ms 간 일시정지합니다.", duration.toMillis());
        Thread.sleep(duration);
        log.info("테스트를 재개합니다.");
    }

    public static AuthTokensDTO extractJwtTokens(MvcResult mvcResult)
        throws UnsupportedEncodingException
    {
        String mvcResultContentString = mvcResult.getResponse().getContentAsString();
        return AuthTokensDTO.builder()
            .accessToken(JsonPath.read(mvcResultContentString, "$.data.accessToken"))
            .refreshToken(JsonPath.read(mvcResultContentString, "$.data.refreshToken"))
            .build();
    }
}
