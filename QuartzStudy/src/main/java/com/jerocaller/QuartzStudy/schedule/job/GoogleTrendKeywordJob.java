package com.jerocaller.QuartzStudy.schedule.job;

import com.jerocaller.QuartzStudy.service.GoogleTrendService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@PersistJobDataAfterExecution
public class GoogleTrendKeywordJob implements Job {

    private final GoogleTrendService googleTrendService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        googleTrendService.saveData();
    }
}
