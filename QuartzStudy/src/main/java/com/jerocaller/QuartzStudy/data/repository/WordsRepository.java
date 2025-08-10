package com.jerocaller.QuartzStudy.data.repository;

import com.jerocaller.QuartzStudy.data.entity.News;
import com.jerocaller.QuartzStudy.data.entity.Words;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordsRepository extends JpaRepository<Words, Integer> {

    @Query("SELECT w FROM Words w WHERE w.news.id = ?1")
    List<Words> findByNewsId(int newsId);

    List<Words> findByNews(News news);
}
