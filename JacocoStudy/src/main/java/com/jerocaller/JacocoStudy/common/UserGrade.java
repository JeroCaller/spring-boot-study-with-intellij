package com.jerocaller.JacocoStudy.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserGrade {
    BRONZE("Bronze"),
    SILVER("Silver"),
    GOLD("Gold");

    private final String userGrade;
}
