package com.jerocaller.TestStudy.study;

import org.junit.jupiter.api.*;

public class JUnitLifeCycle {

    /**
     * 전체 테스트를 실행하기 전 딱 한 번만 실행된다.
     * DB와 같은 외부 시스템과의 연동과 같은 테스트 준비에 사용된다.
     * static으로 선언해야 함.
     */
    @BeforeAll
    static void beforeAll() {
        System.out.println("=== beforeAll 호출 됨 ===");
    }

    /**
     * 각 테스트 케이스(메서드) 실행 전마다 실행됨.
     * 각 테스트 메서드에서 사용될 객체 초기화, 테스트에 필요한 값 주입 등의
     * 테스트 전에 실행되어야 할 기능들을 여기서 설정할 수 있다.
     */
    @BeforeEach
    public void beforeEach() {
        System.out.println("=== beforeEach 호출 됨 ===");
    }

    /**
     * 모든 테스트 종료 후 딱 한 번 실행됨.
     * DB 연결 종료, 공통적으로 사용하는 자원 해제 등의 마무리 작업에 사용.
     * static으로 선언해야 함.
     */
    @AfterAll
    static void afterAll() {
        System.out.println("=== afterAll 호출 됨 ===");
    }

    /**
     * 각 테스트 케이스가 종료될 때마다 실행됨 .
     * 테스트 후 특정 데이터 삭제 등과 같은 각 테스트 케이스마다의 마무리 작업에 사용.
     */
    @AfterEach
    public void afterEach() {
        System.out.println("=== afterEach 호출 됨 ===");
    }

    @Test
    public void test1() {
        System.out.println("----- Test 1 -----");
    }

    @Test
    public void test2() {
        System.out.println("----- Test 2 -----");
    }

    @Test
    public void test3() {
        System.out.println("----- Test 3 -----");
    }

}
