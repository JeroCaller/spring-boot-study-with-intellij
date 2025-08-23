package com.jerocaller.SpringBootScheduling.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * <p>
 *     스케줄러를 관리하는 스레드 설정 클래스.
 * </p>
 * <p>
 *     SpringBoot 제공 scheduler는 기본적으로 싱글 스레드 기반으로 동작한다.
 *     이로 인해 둘 이상의 스케줄러들이 등록되어 작동할 때 지정한 시간 간격대로
 *     수행되지 않고 엉뚱한 시간 간격으로 진행될 수 있다.
 *     이를 방지하기 위해 멀티 스레드로 설정한다.
 * </p>
 */
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    // 다른 클래스에서도 사용할 수 있도록 static으로 선언함.
    private final static int POOL_SIZE = 5;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("config-scheduler");
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
