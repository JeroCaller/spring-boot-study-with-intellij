package com.jerocaller.JacocoStudy.dto;

import com.jerocaller.JacocoStudy.common.UserGrade;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserDto {
    private String username;
    private String email;
    private int money;

    @Setter
    private UserGrade userGrade;

    @Setter
    private int experiencePoint;

    public void addMoney(int increment) {
        money += increment;
    }
}
