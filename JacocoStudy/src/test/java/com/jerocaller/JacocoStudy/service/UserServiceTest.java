package com.jerocaller.JacocoStudy.service;

import com.jerocaller.JacocoStudy.common.UserGrade;
import com.jerocaller.JacocoStudy.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import({UserService.class})
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void addBonusByUserGradeTest() {
        UserDto userDto = UserDto.builder()
            .username("kimquel")
            .email("kimquel@email.com")
            .build();

        userDto = userService.addBonusByUserGrade(userDto);
        assertThat(userDto.getMoney()).isEqualTo(0);
        
        userDto.setUserGrade(UserGrade.BRONZE);
        userDto = userService.addBonusByUserGrade(userDto);
        assertThat(userDto.getMoney()).isEqualTo(100);

        userDto.setUserGrade(UserGrade.SILVER);
        userDto = userService.addBonusByUserGrade(userDto);
        assertThat(userDto.getMoney()).isEqualTo(300);

        userDto.setUserGrade(UserGrade.GOLD);
        userDto = userService.addBonusByUserGrade(userDto);
        assertThat(userDto.getMoney()).isEqualTo(600);
    }

    @Test
    void decideUserGradeTest() {
        UserDto userDto = UserDto.builder()
            .username("kimquel")
            .email("kimquel@email.com")
            .userGrade(UserGrade.SILVER)
            .build();
        UserDto resultUserDto;

        resultUserDto = userService.decideUserGrade(userDto);
        assertThat(resultUserDto.getUserGrade()).isEqualTo(UserGrade.SILVER);

        userDto.setUserGrade(null);
        userDto.setExperiencePoint(10);
        resultUserDto = userService.decideUserGrade(userDto);
        assertThat(resultUserDto.getUserGrade()).isEqualTo(UserGrade.BRONZE);

        userDto.setExperiencePoint(150);
        resultUserDto = userService.decideUserGrade(userDto);
        assertThat(resultUserDto.getUserGrade()).isEqualTo(UserGrade.SILVER);

        userDto.setExperiencePoint(300);
        resultUserDto = userService.decideUserGrade(userDto);
        assertThat(resultUserDto.getUserGrade()).isEqualTo(UserGrade.GOLD);
    }

    @Test
    void getTotalAllUsersMoneyTest() {
        List<UserDto> users = new ArrayList<>();
        users.add(UserDto.builder().money(1000).build());
        users.add(UserDto.builder().money(500).build());

        int result = userService.getTotalAllUsersMoney(users);
        assertThat(result).isEqualTo(1500);
    }
}