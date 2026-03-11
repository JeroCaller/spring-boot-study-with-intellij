package com.jerocaller.AuthTokenSecurity.data.dto.request;

import com.jerocaller.AuthTokenSecurity.validator.annotation.NullableSize;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     유저 정보 갱신 요청 DTO
 * </p>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoPatchRequest {

    @NullableSize(min = 3, max = 30, message = "username은 3글자 이상, 30글자 이하여야합니다.")
    private String username;

    @NullableSize(min = 5, max = 30, message = "기존 패스워드 입력 양식에 문제가 있습니다. " +
        "password는 5글자 이상, 30글자 이하여야합니다.")
    private String beforePassword;

    @NullableSize(min = 5, max = 30, message = "새로 변경할 패스워드 입력 양식에 문제가 있습니다. " +
        "password는 5글자 이상, 30글자 이하여야합니다.")
    private String nextPassword;

    @Nullable
    @Min(value = 20)
    @Max(value = 100)
    private Integer age;
}
