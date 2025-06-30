package com.jerocaller.QuartzStudy.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private HttpStatus httpStatus;
    private String message;
    private T data;

    public ResponseEntity<ApiResponse<T>> toResponseEntity() {
        return ResponseEntity.status(this.getHttpStatus().value())
            .body(this);
    }
}
