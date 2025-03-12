package com.jerocaller.TestStudy.simple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JUnitTest {

    @Test
    @DisplayName("덧셈 테스트")
    public void addTest() {

        int a = 1;
        int b = 2;
        int sum = 3;

        Assertions.assertEquals(sum, a + b);

    }

    /*
    @Test
    @DisplayName("1 + 1 = 3이라고 하면 테스트 실패하는지 확인.")
    public void addFailedTest() {

        int a = 1;
        int b = 1;
        int sum = 3;

        Assertions.assertEquals(sum, a + b);
    }

     */
}
