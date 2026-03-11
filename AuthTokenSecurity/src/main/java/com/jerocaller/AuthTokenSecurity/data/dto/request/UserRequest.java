package com.jerocaller.AuthTokenSecurity.data.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank
    @Size(min = 3, max = 30, message = "username은 3글자 이상, 30글자 이하여야합니다.")
    private String username;

    @NotBlank
    @Size(min = 5, max = 30, message = "password는 5글자 이상, 30글자 이하여야합니다.")
    private String password;

    @Min(value = 20)
    @Max(value = 100)
    private int age;
}
