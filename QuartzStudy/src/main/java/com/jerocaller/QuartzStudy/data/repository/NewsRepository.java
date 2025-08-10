package com.jerocaller.QuartzStudy.data.repository;

import com.jerocaller.QuartzStudy.data.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {

    boolean existsByTitle(String title);

    @Query("SELECT n FROM News n LEFT JOIN Words w ON n.id = w.news.id WHERE w.news IS NULL")
    List<News> findAllNotInWords();

    @Query("SELECT n FROM News n LEFT JOIN Words w ON n.id = w.news.id WHERE w.news IS NOT NULL")
    List<News> findAllInWords();
}
