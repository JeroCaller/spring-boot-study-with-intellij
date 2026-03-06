package com.jerocaller.AuthTokenSecurity.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class TestUtil {
    public static void delay(Duration duration) throws InterruptedException {
        log.info("테스트를 위해 {}ms 간 일시정지합니다.", duration.toMillis());
        Thread.sleep(duration);
        log.info("테스트를 재개합니다.");
    }
}
