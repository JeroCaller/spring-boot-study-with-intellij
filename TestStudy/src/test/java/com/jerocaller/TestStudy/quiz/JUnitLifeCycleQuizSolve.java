package com.jerocaller.TestStudy.quiz;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class JUnitLifeCycleQuizSolve {

    @BeforeEach
    public void hi() {
        log.info("Helllo!");
    }

    @Test
    public void test1() {
        log.info("This is First Test");
    }

    @Test
    public void test2() {
        log.info("This is Second Test");
    }

    @AfterAll
    static void bye() {
        log.info("Bye!");
    }

}
