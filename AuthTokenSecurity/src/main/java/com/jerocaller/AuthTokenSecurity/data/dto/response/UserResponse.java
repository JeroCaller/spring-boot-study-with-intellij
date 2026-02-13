package com.jerocaller.AuthTokenSecurity.data.dto.response;

import com.jerocaller.AuthTokenSecurity.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String username;
    private Integer age;
    private LocalDateTime registerDate;
    private LocalDateTime lastInfoUpdatedAt;
    private LocalDateTime lastLoginAt;

    public static UserResponse toDto(User userEntity) {
        return UserResponse.builder()
            .username(userEntity.getUsername())
            .age(userEntity.getAge())
            .registerDate(userEntity.getCreatedAt())
            .lastInfoUpdatedAt(userEntity.getUpdatedAt())
            .lastLoginAt(userEntity.getLastLoginAt())
            .build();
    }
}
