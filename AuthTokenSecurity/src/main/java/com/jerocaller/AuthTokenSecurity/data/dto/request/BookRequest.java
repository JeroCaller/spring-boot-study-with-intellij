package com.jerocaller.AuthTokenSecurity.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {
    private String name;
    private Integer price;
}
