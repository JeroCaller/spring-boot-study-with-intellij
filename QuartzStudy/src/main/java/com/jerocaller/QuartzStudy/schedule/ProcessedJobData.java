package com.jerocaller.QuartzStudy.schedule;

import com.jerocaller.QuartzStudy.mapper.DateMapper;
import lombok.Getter;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

import java.time.LocalDateTime;

@Getter
public class ProcessedJobData {

    private JobExecutionContext context;
    private String jobName;
    private Integer counter;
    private LocalDateTime fireDateTime;
    private String fireDateTimeToString;

    public ProcessedJobData(JobExecutionContext context) {
        this.context = context;

        process();
    }

    private void process() {
        JobDetail jobDetail = context.getJobDetail();
        jobName = jobDetail.getKey().getName();
        counter = jobDetail.getJobDataMap().getInt(ScheduleNames.COUNTER);
        fireDateTime = DateMapper.toLocalDateTime(context.getFireTime());
        fireDateTimeToString = DateMapper.toDateTimeFormatString(fireDateTime);
    }

    public void persistNextCounter() {
        context.getJobDetail().getJobDataMap()
            .put(ScheduleNames.COUNTER, counter + 1);
    }
}
