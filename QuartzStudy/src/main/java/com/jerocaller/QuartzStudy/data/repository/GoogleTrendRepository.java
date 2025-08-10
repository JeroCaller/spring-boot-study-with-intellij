package com.jerocaller.QuartzStudy.data.repository;

import com.jerocaller.QuartzStudy.data.entity.GoogleTrend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleTrendRepository extends JpaRepository<GoogleTrend, Integer> {

    Optional<GoogleTrend> findByTitle(String title);

}
