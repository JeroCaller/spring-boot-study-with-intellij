package com.jerocaller.QuartzStudy.schedule.job.listener;

import com.jerocaller.QuartzStudy.mapper.DateMapper;
import com.jerocaller.QuartzStudy.schedule.ProcessedJobData;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class GlobalJobListener implements JobListener {

    @Override
    public String getName() {
        return "GlobalJobListener";
    }

    /**
     * Job 실행 전 수행
     *
     * @param context
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        ProcessedJobData processedJobData = new ProcessedJobData(context);

        log.info("{}: {}번째 {} job 실행 개시",
            processedJobData.getFireDateTimeToString(),
            processedJobData.getCounter(),
            processedJobData.getJobName()
        );
    }

    /**
     * Job 실행 실패 또는 중단 시 수행
     *
     * @param context
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        ProcessedJobData processedJobData = new ProcessedJobData(context);

        log.error("{} job 실행 실패.", processedJobData.getJobName());

        processedJobData.persistNextCounter();
    }

    /**
     * Job 실행 이후 수행
     *
     * @param context
     * @param jobException
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        ProcessedJobData processedJobData = new ProcessedJobData(context);
        String currentDateTimeString = DateMapper.toDateTimeFormatString(LocalDateTime.now());

        log.info("{}: {}번째 {} job 실행 완료.",
            currentDateTimeString,
            processedJobData.getCounter(),
            processedJobData.getJobName()
        );

        processedJobData.persistNextCounter();
    }
}
