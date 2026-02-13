package com.jerocaller.AuthTokenSecurity.controller;

import com.jerocaller.AuthTokenSecurity.business.UserService;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserInfoPatchRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.request.UserRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.RestResponse;
import com.jerocaller.AuthTokenSecurity.data.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final UserService userService;

    /**
     * <p>특정 유저 조회</p>
     * <p>민감하지 않은 정보만 조회. 미인증 사용자도 조회 가능.</p>
     *
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public ResponseEntity<RestResponse.DetailedRestResponse<UserResponse>> inquery(
        @PathVariable("username") String username
    ) {
        UserResponse userResponse = userService.getUserInfo(username);
        return RestResponse.success(userResponse);
    }

    @PostMapping
    public ResponseEntity<RestResponse.DetailedRestResponse<UserResponse>> register(
        @Valid @RequestBody UserRequest userRequest
    ) {
        UserResponse userResponse = userService.register(userRequest);
        return RestResponse.success(userResponse);
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
