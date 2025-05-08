package com.jerocaller.APIForFormData.business;

import com.jerocaller.APIForFormData.data.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    /**
     * 요청으로 들어온 유저 정보 텍스트에 꾸미는 텍스트를 추가하여 반환.
     *
     * @param userRequest
     * @return
     */
    public UserDto decorateData(UserDto userRequest) {
        return UserDto.builder()
            .username(getDecoratedText(userRequest.getUsername()))
            .message(getDecoratedText(userRequest.getMessage()))
            .build();
    }

    private String getDecoratedText(String original) {
        return String.format("===== %s =====", original);
    }

}
