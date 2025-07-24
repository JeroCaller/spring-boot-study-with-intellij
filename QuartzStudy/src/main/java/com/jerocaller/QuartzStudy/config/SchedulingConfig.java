package com.jerocaller.QuartzStudy.config;

import com.jerocaller.QuartzStudy.schedule.ScheduleNames;
import com.jerocaller.QuartzStudy.schedule.job.GoogleTrendKeywordJob;
import com.jerocaller.QuartzStudy.schedule.job.listener.GlobalJobListener;
import com.jerocaller.QuartzStudy.schedule.job.listener.GlobalTriggerListener;
import lombok.RequiredArgsConstructor;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@RequiredArgsConstructor
public class SchedulingConfig implements SchedulerFactoryBeanCustomizer {

    private final GlobalJobListener globalJobListener;
    private final GlobalTriggerListener globalTriggerListener;

    /**
     * 자동 구성될 스케줄러 빈을 초기화하기전 커스텀하기.
     * 여기서는 jobListener를 등록한다.
     * 이 메서드에서 커스텀 설정이 적용된 후, 해당 스케줄러 빈이 초기화된다.
     *
     * @param schedulerFactoryBean
     */
    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        schedulerFactoryBean.setGlobalJobListeners(globalJobListener);
        schedulerFactoryBean.setGlobalTriggerListeners(globalTriggerListener);
    }

    /**
     * org.quartz.SchedulerException: Jobs added with no trigger must be durable.
     * 위 에러 방지를 위해 `.storeDurably(true)`를 추가해야 한다.
     *
     * @return
     */
    @Bean
    public JobDetail GoogleTrendJobDetail() {
        return JobBuilder.newJob(GoogleTrendKeywordJob.class)
            .withIdentity("googleTrendKeywordJob")
            .usingJobData(ScheduleNames.COUNTER, 1)
            .storeDurably(true) // job이 trigger와 분리되어도 저장되도록 함.
            .build();
    }

    @Bean
    public Trigger googleTrendJobTrigger(JobDetail googleTrendJobDetail) {
        return TriggerBuilder.newTrigger()
            .withIdentity("googleTrendKeywordJobTrigger")
            .forJob(googleTrendJobDetail)  // 트리거 할 jobDetail 등록
            .startNow()
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMinutes(1)
                    .withRepeatCount(3)
            )
            .build();
    }
}
