package com.jerocaller.QuartzStudy.config;

import com.jerocaller.QuartzStudy.schedule.ScheduleNames;
import com.jerocaller.QuartzStudy.schedule.job.GoogleTrendKeywordJob;
import com.jerocaller.QuartzStudy.schedule.job.SimilarityJob;
import com.jerocaller.QuartzStudy.schedule.job.WordAnalysisJob;
import com.jerocaller.QuartzStudy.schedule.listener.GlobalJobListener;
import com.jerocaller.QuartzStudy.schedule.listener.GlobalTriggerListener;
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

/**
 * <h3>Note</h3>
 * <p>
 *     스프링부트에서는 Quartz의 Scheduler 객체가 auto-configure되며,
 *     JobDetail, Trigger, Calendar 타입의 Bean들을 자동으로 찾아 Scheduler 객체에
 *     등록한다고 한다. 따라서 JobDetail과 Trigger는 각자 별도로 Bean으로 등록해야
 *     에러 없이 작동 가능하다. 만약 하나의 메서드에 JobDetail, Trigger 객체를 모두 생성하고
 *     Trigger 객체만 반환하는 방식을 취한다면 JobDetail 객체는 Spring Bean에 등록되지 않으므로
 *     에러가 발생한다.
 * </p>
 */
@Configuration
@RequiredArgsConstructor
public class SchedulingConfig implements SchedulerFactoryBeanCustomizer {

    private final GlobalJobListener globalJobListener;
    private final GlobalTriggerListener globalTriggerListener;

    private final int JOB_REPEAT = 3;

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
    public JobDetail googleTrendJobDetail() {
        return JobBuilder.newJob(GoogleTrendKeywordJob.class)
            .withIdentity("googleTrendKeywordJob")
            .usingJobData(ScheduleNames.COUNTER, 1)
            .storeDurably(true) // job이 trigger와 분리되어도 저장되도록 함.
            .build();
    }

    @Bean
    public Trigger googleTrendJobTrigger() {
        return TriggerBuilder.newTrigger()
            .withIdentity("googleTrendKeywordJobTrigger")
            .forJob(googleTrendJobDetail())  // 트리거 할 jobDetail 등록
            .startNow()
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMinutes(1)
                    .withRepeatCount(JOB_REPEAT)
            )
            .build();
    }

    @Bean
    public JobDetail wordAnalysisJobDetail() {
        return JobBuilder.newJob(WordAnalysisJob.class)
            .withIdentity("wordAnalysisJob")
            .usingJobData(ScheduleNames.COUNTER, 1)
            .storeDurably(true)
            .build();
    }

    @Bean
    public Trigger wordAnalysisJobTrigger() {
        return TriggerBuilder.newTrigger()
            .withIdentity("wordAnalysisJobTrigger")
            .forJob(wordAnalysisJobDetail())
            .startNow()
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(30)
                    .withRepeatCount(JOB_REPEAT)
            )
            .build();
    }

    @Bean
    public JobDetail similarityJobDetail() {
        return JobBuilder.newJob(SimilarityJob.class)
            .withIdentity("similarityJob")
            .usingJobData(ScheduleNames.COUNTER, 1)
            .storeDurably(true)
            .build();
    }

    @Bean
    public Trigger similarityJobTrigger() {
        return TriggerBuilder.newTrigger()
            .withIdentity("similarityJobTrigger")
            .forJob(similarityJobDetail())
            .startNow()
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(45)
                    .withRepeatCount(JOB_REPEAT)
            )
            .build();
    }
}
