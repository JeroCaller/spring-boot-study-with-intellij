package com.jerocaller.JacocoStudy.service;

import com.jerocaller.JacocoStudy.common.UserGrade;
import com.jerocaller.JacocoStudy.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    public UserDto addBonusByUserGrade(UserDto userDto) {
        if (userDto.getUserGrade() == null) {
            return userDto;
        }

        switch (userDto.getUserGrade()) {
            case UserGrade.BRONZE:
                userDto.addMoney(100);
                break;
            case UserGrade.SILVER:
                userDto.addMoney(200);
                break;
            case UserGrade.GOLD:
                userDto.addMoney(300);
                break;
        }

        return userDto;
    }

    public UserDto decideUserGrade(UserDto userDto) {
        if (userDto.getUserGrade() != null) {
            return userDto;
        }

        UserGrade userGrade;

        if (userDto.getExperiencePoint() <= 100) {
            userGrade = UserGrade.BRONZE;
        } else if (userDto.getExperiencePoint() <= 200) {
            userGrade = UserGrade.SILVER;
        } else {
            userGrade = UserGrade.GOLD;
        }

        return UserDto.builder()
            .username(userDto.getUsername())
            .email(userDto.getEmail())
            .experiencePoint(userDto.getExperiencePoint())
            .money(userDto.getMoney())
            .userGrade(userGrade)
            .build();
    }

    public int getTotalAllUsersMoney(List<UserDto> users) {
        int total = 0;

        for (UserDto userDto : users) {
            total += userDto.getMoney();
        }

        return total;
    }

    public int getTotalAllUsersExp(List<UserDto> users) {
        int total = 0;

        for (UserDto userDto : users) {
            total += userDto.getExperiencePoint();
        }

        return total;
    }
}
