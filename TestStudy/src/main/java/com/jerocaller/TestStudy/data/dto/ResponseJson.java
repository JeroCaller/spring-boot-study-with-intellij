package com.jerocaller.TestStudy.data.dto;

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
public class ResponseJson {

    private HttpStatus httpStatus;
    private String message;
    private Object data;

    public ResponseEntity<ResponseJson> toResponseEntity() {
        return ResponseEntity.status(this.getHttpStatus().value())
            .body(this);
    }
}
