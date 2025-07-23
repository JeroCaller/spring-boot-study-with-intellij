package com.jerocaller.QuartzStudy.schedule.job;

import com.jerocaller.QuartzStudy.service.WordAnalyzeService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@PersistJobDataAfterExecution
public class WordAnalysisJob implements Job {

    private final WordAnalyzeService wordAnalyzeService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        wordAnalyzeService.saveAnalyzedWords();
    }
}
