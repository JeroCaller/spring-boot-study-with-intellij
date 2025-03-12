package com.jerocaller.TestStudy.study;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
//import org.assertj.core.api.Assertions;

/**
 * AssertJ 라이브러리에서 제공해주는 메서드 확인
 */
public class JUnitAssertJ {

    @Test
    public void isEqualTo() {
        int a = 1;
        int b = 2;
        int sum = 3;

        //Assertions.assertThat()
        assertThat(a + b).isEqualTo(sum);
    }

    @Test
    public void isNotEqualTo() {
        int a = 1;
        int b = 1;
        int sum = 3;

        assertThat(a + b).isNotEqualTo(sum);
    }

    @Test
    public void containsMethod() {
        int target = 1;
        int[] array = {1, 2, 3};

        assertThat(array).contains(target);
    }

    @Test
    public void doesNotContainMethod() {
        int target = 4;
        int[] array = {1, 2, 3};

        assertThat(array).doesNotContain(target);
    }

    @Test
    public void startsWithMethod() {
        String source = "Bearer abc123";
        String target = "Bearer";

        assertThat(source).startsWith(target);
    }

    @Test
    public void endsWithMethod() {
        String source = "안녕하십니까?";
        String target = "?";

        assertThat(source).endsWith(target);
    }

    @Test
    public void isEmptyMethod() {
        int[] array = {};

        assertThat(array).isEmpty();
    }

    @Test
    public void isNotEmptyMethod() {
        List<Integer> original = new ArrayList<>();
        List<Integer> other = new ArrayList<>();
        other.add(1);
        original = other; // 객체의 참조값 대입. 두 변수는 같은 객체를 참조하게 됨.

        assertThat(original).isNotEmpty();
        assertThat(original).isEqualTo(other); // 두 변수는 같은 참조값을 가지는가?
    }

    @Test
    public void isPositiveMethod() {
        int a = 3;

        assertThat(a).isPositive();
    }

    @Test
    public void isNegativeMethod() {
        int a = -1;

        assertThat(a).isNegative();
    }

    @Test
    public void isGreaterThanMethod() {
        int[] source = {3, 2, 1};
        int[] expected = {1, 2, 3};

        int[] actual = bubbleSortAsc(source);
        printIntArray(actual);

        assertThat(actual).isEqualTo(expected);
        assertThat(actual[1]).isGreaterThan(actual[0]);
    }

    @Test
    public void isLessThenMethod() {
        int[] source = {3, 2, 1};
        int[] expected = {1, 2, 3};

        int[] actual = bubbleSortAsc(source);
        printIntArray(actual);

        assertThat(actual).isEqualTo(expected);
        assertThat(actual[1]).isLessThan(actual[2]);
    }

    private int[] bubbleSortAsc(int[] array) {

        int[] copiedArray = Arrays.copyOf(array, array.length);
        for (int i = 0; i < copiedArray.length; i++) {
            int endIndex = copiedArray.length - i;
            for (int j = 0; j < endIndex - 1; j++) {
                if (copiedArray[j] > copiedArray[j+1]) {
                    int temp = copiedArray[j+1];
                    copiedArray[j+1] = copiedArray[j];
                    copiedArray[j] = temp;
                }
            }
        }

        return copiedArray;
    }

    private void printIntArray(int[] array) {

        StringBuilder stringBuilder = new StringBuilder("[");

        for (int i = 0; i < array.length; i++) {
            stringBuilder.append(array[i]);
            if (i != array.length - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");

        System.out.println(stringBuilder.toString());

    }

}
