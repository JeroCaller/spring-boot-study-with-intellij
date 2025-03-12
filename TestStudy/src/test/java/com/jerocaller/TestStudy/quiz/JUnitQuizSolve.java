package com.jerocaller.TestStudy.quiz;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class JUnitQuizSolve {

    @Test
    public void test1() {
        String name1 = "반가";
        String name2 = "반가";
        String name3 = "반갑";

        List<String> names = Arrays.asList(name1, name2, name3);
        names.forEach(name -> {
            assertThat(name).isNotEmpty();
            assertThat(name).isNotNull();
        });

        assertThat(name1).isEqualTo(name2);
        assertThat(name2).isNotEqualTo(name3);

    }

    @Test
    public void test2() {

        int number1 = 10;
        int number2 = 0;
        int number3 = -5;

        assertThat(number1).isPositive();
        assertThat(number2).isZero();
        assertThat(number3).isNegative();
        assertThat(number1).isGreaterThan(number2);
        assertThat(number3).isLessThan(number2);

    }

}
