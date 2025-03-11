package com.jerocaller.FirstSpringBootAtIntelliJ.controller;

import com.jerocaller.FirstSpringBootAtIntelliJ.business.UserService;
import com.jerocaller.FirstSpringBootAtIntelliJ.common.ResponseMessages;
import com.jerocaller.FirstSpringBootAtIntelliJ.data.dto.ResponseJson;
import com.jerocaller.FirstSpringBootAtIntelliJ.data.entity.ClassType;
import com.jerocaller.FirstSpringBootAtIntelliJ.data.entity.SiteUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/types")
    public ResponseEntity<ResponseJson> getAllClassTypes() {

        List<ClassType> classTypes = userService.getAllClassTypes();

        return ResponseJson.builder()
            .httpStatus(HttpStatus.OK)
            .message(ResponseMessages.READ_OK)
            .data(classTypes)
            .build()
            .toResponseEntity();
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseJson> getAllUsers() {

        List<SiteUsers> users = userService.getAllUsers();

        return ResponseJson.builder()
            .httpStatus(HttpStatus.OK)
            .message(ResponseMessages.READ_OK)
            .data(users)
            .build()
            .toResponseEntity();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResponseJson> getUserById(
        @PathVariable("id") int id
    ) {

        SiteUsers user = userService.getOneUserById(id);

        return ResponseJson.builder()
            .httpStatus(HttpStatus.OK)
            .message(ResponseMessages.READ_OK)
            .data(user)
            .build()
            .toResponseEntity();
    }

    @GetMapping("/users/type/{typeId}")
    public ResponseEntity<ResponseJson> getUsersByClassType(
        @PathVariable("typeId") int typeId
    ) {

        List<SiteUsers> users = userService.getUsersByClassType(typeId);

        return ResponseJson.builder()
            .httpStatus(HttpStatus.OK)
            .message(ResponseMessages.READ_OK)
            .data(users)
            .build()
            .toResponseEntity();
    }

    @GetMapping("/users/recommendations/most")
    public ResponseEntity<ResponseJson> getMostRecommendedUser() {

        SiteUsers user = userService.getUserByMaxRecomm();

        return ResponseJson.builder()
            .httpStatus(HttpStatus.OK)
            .message(ResponseMessages.READ_OK)
            .data(user)
            .build()
            .toResponseEntity();
    }

}
