package com.jerocaller.QuartzStudy.data.repository;

import com.jerocaller.QuartzStudy.data.entity.Words;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordsRepository extends JpaRepository<Words, Integer> {
}
