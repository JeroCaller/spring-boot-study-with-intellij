package com.jerocaller.QuartzStudy.controller;

import com.jerocaller.QuartzStudy.http.response.ApiResponse;
import com.jerocaller.QuartzStudy.service.SimilarityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/similarity")
@RequiredArgsConstructor
public class SimilarityController {

    private final SimilarityService similarityService;

    @GetMapping("/news/source/{sourceId}/target/{targetId}")
    public ResponseEntity<ApiResponse<Object>> getSimilarityBetweenTwoNews(
        @PathVariable("sourceId") int sourceId,
        @PathVariable("targetId") int targetId
    ) {
        double result = similarityService.getJaccardBetweenTwoNews(sourceId, targetId);

        return ApiResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("유사도 계산 성공")
            .data(result)
            .build()
            .toResponseEntity();
    }

    @PostMapping("/news")
    public ResponseEntity<ApiResponse<Object>> calculateAndSaveSimilarityBetweenNews() {
        similarityService.saveJaccardBetweenNews();

        return ApiResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("유사도 계산 및 저장 성공")
            .build()
            .toResponseEntity();
    }
}
