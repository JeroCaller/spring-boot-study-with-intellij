package com.jerocaller.AuthTokenSecurity.exception.handler;

import com.jerocaller.AuthTokenSecurity.data.dto.response.ResponseCode;
import com.jerocaller.AuthTokenSecurity.data.dto.response.RestResponse;
import com.jerocaller.AuthTokenSecurity.exception.custom.BaseCustomException;
import com.jerocaller.libs.spoonsuits.web.validation.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<RestResponse.DetailedRestResponse<Object>> handleUnexpectedExcetpion(Exception e) {
        log.error("예상치 못한 예외 발생.");

        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            log.error(stackTraceElement.toString());
        }

        return RestResponse.error(ResponseCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<RestResponse.DetailedRestResponse<Object>> handleUsernameNotFoundException(
        UsernameNotFoundException e
    ) {
        return RestResponse.error(ResponseCode.USER_NOT_FOUND);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<RestResponse.DetailedRestResponse<Object>> handleBadCredentialException(
        BadCredentialsException e
    ) {
        return RestResponse.error(ResponseCode.AUTHENTICATION_FAILED);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse.DetailedRestResponse<Object>> handleValidationException(
        MethodArgumentNotValidException e
    ) {
        return RestResponse.error(
            ValidationUtils.getValidationFailedMessage(e),
            ResponseCode.INVALID_USER_FORMAT
        );
    }

    @ExceptionHandler(value = BaseCustomException.class)
    public ResponseEntity<RestResponse.DetailedRestResponse<Object>> handleCustomExceptions(BaseCustomException e) {
        return RestResponse.error(e.getResponseCode());
    }
}
