package com.jerocaller.TestStudy.exception;

import com.jerocaller.TestStudy.data.dto.ResponseJson;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ResponseJson> handleEntityNotFoundException(
        EntityNotFoundException e
    ) {

        log.error("=== EntityNotFoundException 예외 처리 후 JSON 응답 처리함 ===");

        return ResponseJson.builder()
            .httpStatus(HttpStatus.NOT_FOUND)
            .message(e.getMessage())
            .build()
            .toResponseEntity();
    }

}
