package com.jerocaller.QuartzStudy.service;

import com.jerocaller.QuartzStudy.data.entity.News;
import com.jerocaller.QuartzStudy.data.entity.Words;
import com.jerocaller.QuartzStudy.data.repository.NewsRepository;
import com.jerocaller.QuartzStudy.data.repository.WordsRepository;
import jakarta.transaction.Transactional;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WordAnalyzeService {

    private final NewsRepository newsRepository;
    private final WordsRepository wordsRepository;
    private final Komoran komoran;

    private final List<String> allowedTags = Arrays.asList(
        "NNG", "NNP", "NNB", "XR", "VV", "SL"
    );

    @Transactional
    public void saveAnalyzedWords() {
        List<News> notAnalyzedNews = newsRepository.findAllNotInWords();

        for (News news : notAnalyzedNews) {
            KomoranResult komoranResult = komoran.analyze(news.getTitle());
            List<String> words = komoranResult.getMorphesByTags(allowedTags);

            for (String word : words) {
                wordsRepository.save(Words.builder()
                    .word(word)
                    .news(news)
                    .build()
                );
            }
        }
    }
}
