package com.jerocaller.AuthTokenSecurity.controller;

import com.jerocaller.AuthTokenSecurity.data.dto.request.UserInfoPatchRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.RestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    /**
     * <p>특정 유저 조회</p>
     * <p>민감하지 않은 정보만 조회. 미인증 사용자도 조회 가능.</p>
     *
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public RestResponse inquery(@PathVariable("username") String username) {
        return null;
    }

    @PostMapping
    public RestResponse register(@Valid @RequestBody UserRequest userRequest) {
        return null;
    }

    @PatchMapping
    public RestResponse updateUserInfo(
        @Valid @RequestBody UserInfoPatchRequest userInfoPatchRequest) {
        return null;
    }

    @DeleteMapping
    public RestResponse unregister() {
        return null;
    }
}
