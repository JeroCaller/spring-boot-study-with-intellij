package com.jerocaller.QuartzStudy.controller;

import com.jerocaller.QuartzStudy.http.response.ApiResponse;
import com.jerocaller.QuartzStudy.service.WordAnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analysis/words")
@RequiredArgsConstructor
public class WordAnalysisController {

    private final WordAnalyzeService wordAnalyzeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> analyzeNewsTitleToSaveWords() {
        wordAnalyzeService.saveAnalyzedWords();

        return ApiResponse.builder()
            .httpStatus(HttpStatus.OK)
            .message("뉴스 타이틀로부터 태그 분석 완료 및 DB 저장 완료")
            .build()
            .toResponseEntity();
    }
}
