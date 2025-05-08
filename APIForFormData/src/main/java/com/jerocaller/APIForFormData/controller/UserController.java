package com.jerocaller.APIForFormData.controller;

import com.jerocaller.APIForFormData.business.UserService;
import com.jerocaller.APIForFormData.data.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Object> formDataPost(@RequestBody UserDto userRequest) {

        UserDto userResponse = userService.decorateData(userRequest);

        return ResponseEntity.ok().body(userResponse);
    }

}
