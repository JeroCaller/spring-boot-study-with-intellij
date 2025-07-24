package com.jerocaller.QuartzStudy.service;

import com.jerocaller.QuartzStudy.data.entity.News;
import com.jerocaller.QuartzStudy.data.entity.Similarity;
import com.jerocaller.QuartzStudy.data.entity.Words;
import com.jerocaller.QuartzStudy.data.repository.NewsRepository;
import com.jerocaller.QuartzStudy.data.repository.SimilarityRepository;
import com.jerocaller.QuartzStudy.data.repository.WordsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimilarityService {

    private final SimilarityRepository similarityRepository;
    private final WordsRepository wordsRepository;
    private final NewsRepository newsRepository;

    // newsId, words
    private final Map<Integer, List<Words>> memo = new HashMap<>();

    public double getJaccardSimilarity(Set<String> source, Set<String> target) {
        Set<String> intersection = new HashSet<>(source);
        intersection.retainAll(target);

        Set<String> union = new HashSet<>(source);
        union.addAll(target);

        /*
        log.info("==========");
        log.info("source: {}", source);
        log.info("target: {}", target);
        log.info("Intersection : {}", intersection);
        log.info("Union: {}", union);
        log.info("==========");

         */

        return (double) intersection.size() / union.size();
    }

    public double getJaccardBetweenTwoNews(int newsSourceId, int newsTargetId) {
        List<Words> sourceWords = wordsRepository.findByNewsId(newsSourceId);
        List<Words> targetWords = wordsRepository.findByNewsId(newsTargetId);

        if (sourceWords == null || targetWords == null) {
            return -1.000;
        }

        Set<String> sourceWordsString = toStringWords(sourceWords);
        Set<String> targetWordsString = toStringWords(targetWords);
        return getJaccardSimilarity(sourceWordsString, targetWordsString);
    }

    public Map<String, Object> getJaccardInfo(int newsSourceId, int newsTargetId) {
        Map<String, Object> responseData = new HashMap<>();

        List<Words> sourceWords = wordsRepository.findByNewsId(newsSourceId);
        List<Words> targetWords = wordsRepository.findByNewsId(newsTargetId);

        if (sourceWords == null || targetWords == null) {
            responseData.put("info", "뉴스 데이터를 얻어올 수 없습니다.");
            return responseData;
        }

        Set<String> sourceWordsString = toStringWords(sourceWords);
        Set<String> targetWordsString = toStringWords(targetWords);
        Set<String> intersection = new HashSet<>(sourceWordsString);
        intersection.retainAll(targetWordsString);

        responseData.put("sourceNewsWords", sourceWordsString);
        responseData.put("targetNewsWords", targetWordsString);
        responseData.put("intersection", intersection);
        responseData.put("sourceNewsInfo", getNewsInfo(newsSourceId));
        responseData.put("targetNewsInfo", getNewsInfo(newsTargetId));
        return responseData;
    }

    @Transactional
    public void saveJaccardBetweenNews() {
        memo.clear();
        List<News> manyNews = newsRepository.findAllInWords();

        for (News newsSource : manyNews) {
            for (News newsTarget : manyNews) {
                if (newsSource.getId() >= newsTarget.getId()) {
                    continue;
                }

                if (similarityRepository.existsBySourceNewsAndTargetNews(newsSource, newsTarget) ||
                    similarityRepository.existsBySourceNewsAndTargetNews(newsTarget, newsSource)) {
                    continue;
                }

                List<Words> sourceWords = getWordsEntitiesByMemo(newsSource);
                List<Words> targetWords = getWordsEntitiesByMemo(newsTarget);
                double similarity = getJaccardSimilarity(
                    toStringWords(sourceWords),
                    toStringWords(targetWords)
                );

                if (similarity > 0.0) {
                    similarityRepository.save(Similarity.builder()
                        .similarityValue(similarity)
                        .sourceNews(newsSource)
                        .targetNews(newsTarget)
                        .build()
                    );
                }
            }
        }

        memo.clear();
    }

    private Set<String> toStringWords(List<Words> words) {
        Set<String> wordString = new HashSet<>();

        words.forEach(w -> {
            wordString.add(w.getWord());
        });
        return wordString;
    }

    private List<Words> getWordsEntitiesByMemo(News news) {
        if (!memo.containsKey(news.getId())) {
            List<Words> words = wordsRepository.findByNews(news);
            memo.put(news.getId(), words);
            return words;
        }
        return memo.get(news.getId());
    }

    private Map<String, Object> getNewsInfo(int newsId) {
        Map<String, Object> newsData = new HashMap<>();
        Optional<News> newsOpt = newsRepository.findById(newsId);

        if (newsOpt.isEmpty()) {
            return null;
        }

        News news = newsOpt.get();
        newsData.put("title", news.getTitle());
        newsData.put("publisher", news.getNewsSource());
        return newsData;
    }
}
