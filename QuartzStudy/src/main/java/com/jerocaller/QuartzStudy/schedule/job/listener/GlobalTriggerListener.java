package com.jerocaller.QuartzStudy.schedule.job.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GlobalTriggerListener implements TriggerListener {

    @Override
    public String getName() {
        return "GlobalTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {}

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {}

    @Override
    public void triggerComplete(
        Trigger trigger,
        JobExecutionContext context,
        Trigger.CompletedExecutionInstruction triggerInstructionCode
    ) {
        log.info("==== 트리거 실행 완료 ====");

        String jobName = context.getJobDetail()
            .getKey()
            .getName();
        String triggerName = trigger.getKey()
            .getName();

        if (!trigger.mayFireAgain()) {
            log.info("{} Trigger는 더 이상 {} job을 트리거하지 않습니다.", triggerName, jobName);
        }
    }
}
