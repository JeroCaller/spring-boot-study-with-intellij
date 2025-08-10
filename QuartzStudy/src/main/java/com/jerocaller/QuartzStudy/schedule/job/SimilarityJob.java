package com.jerocaller.QuartzStudy.schedule.job;

import com.jerocaller.QuartzStudy.service.SimilarityService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@PersistJobDataAfterExecution
public class SimilarityJob implements Job {

    private final SimilarityService similarityService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        similarityService.saveJaccardBetweenNews();
    }
}
