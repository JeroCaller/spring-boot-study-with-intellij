package com.jerocaller.QuartzStudy.data.repository;

import com.jerocaller.QuartzStudy.data.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Integer> {

    boolean existsByTitle(String title);
}
