package com.jerocaller.AuthTokenSecurity.data.dto.response;

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
public class RestResponse<T> {
    private ResponseCode responseCode;
    private T data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailedRestResponse<T> {
        private HttpStatus httpStatus;
        private String code;
        private String message;
        private T data;

        public static <T> DetailedRestResponse<T> toDetailedRestResponse(RestResponse<T> restResponse) {
            return DetailedRestResponse.<T>builder()
                .httpStatus(restResponse.getResponseCode().getHttpStatus())
                .code(restResponse.getResponseCode().getCode())
                .message(restResponse.getResponseCode().getMessage())
                .data(restResponse.getData())
                .build();
        }
    }

    public static <T> ResponseEntity<DetailedRestResponse<T>> success(T data) {
        RestResponse<T> restResponse = RestResponse.<T>builder()
            .responseCode(ResponseCode.OK)
            .data(data)
            .build();
        return toResponseEntity(DetailedRestResponse.toDetailedRestResponse(restResponse));
    }

    public static <T> ResponseEntity<DetailedRestResponse<T>> success(T data, String message) {
        ResponseCode successCode = ResponseCode.OK;
        DetailedRestResponse<T> detailedRestResponse = DetailedRestResponse.<T>builder()
            .httpStatus(successCode.getHttpStatus())
            .code(successCode.getCode())
            .message(message)
            .data(data)
            .build();
        return toResponseEntity(detailedRestResponse);
    }

    public static <T> ResponseEntity<DetailedRestResponse<T>> error(ResponseCode responseCode) {
        RestResponse<T> restResponse = RestResponse.<T>builder()
            .responseCode(responseCode)
            .build();
        return toResponseEntity(DetailedRestResponse.toDetailedRestResponse(restResponse));
    }

    public static <T> ResponseEntity<DetailedRestResponse<T>> error(T data, ResponseCode responseCode) {
        RestResponse<T> restResponse = RestResponse.<T>builder()
            .responseCode(responseCode)
            .data(data)
            .build();
        return toResponseEntity(DetailedRestResponse.toDetailedRestResponse(restResponse));
    }

    private static <T> ResponseEntity<DetailedRestResponse<T>> toResponseEntity(
        DetailedRestResponse<T> detailedRestResponse
    ) {
        return ResponseEntity.status(detailedRestResponse.getHttpStatus())
            .body(detailedRestResponse);
    }
}
