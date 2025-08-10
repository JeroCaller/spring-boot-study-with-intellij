package com.jerocaller.QuartzStudy.data.repository;

import com.jerocaller.QuartzStudy.data.entity.News;
import com.jerocaller.QuartzStudy.data.entity.Similarity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimilarityRepository extends JpaRepository<Similarity, Integer> {

    boolean existsBySourceNewsAndTargetNews(News source, News target);
}
