package com.jerocaller.QuartzStudy.controller;

import com.jerocaller.QuartzStudy.feign.response.RssWrapper;
import com.jerocaller.QuartzStudy.http.response.ApiResponse;
import com.jerocaller.QuartzStudy.service.GoogleTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/api/google-trend")
@RequiredArgsConstructor
public class GoogleTrendController {

    private final GoogleTrendService googleTrendService;

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getSearchResult() {
        RssWrapper result = googleTrendService.getResult();

        return ApiResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("검색 성공")
            .data(result)
            .build()
            .toResponseEntity();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> saveSearchResult() {
        googleTrendService.saveData();

        return ApiResponse.builder()
            .httpStatus(HttpStatus.CREATED)
            .message("DB 저장 성공")
            .build()
            .toResponseEntity();
    }

}
