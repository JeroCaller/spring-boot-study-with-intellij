package com.jerocaller.QuartzStudy.schedule.listener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        ProcessedTriggerData processedTriggerData = getProcessedTriggerData(trigger, context);
        log.info("{} Trigger가 {} Job을 트리거하였습니다.",
            processedTriggerData.getTriggerName(),
            processedTriggerData.getJobName()
        );
    }

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
        ProcessedTriggerData processedTriggerData = getProcessedTriggerData(trigger, context);

        log.info("==== {} 트리거 실행 완료 ====", processedTriggerData.getTriggerName());

        if (!trigger.mayFireAgain()) {
            log.info("{} Trigger는 더 이상 {} job을 트리거하지 않습니다.",
                processedTriggerData.getTriggerName(),
                processedTriggerData.getJobName()
            );
        }
    }

    private ProcessedTriggerData getProcessedTriggerData(
        Trigger trigger,
        JobExecutionContext context
    ) {
        String jobName = context.getJobDetail()
            .getKey()
            .getName();
        String triggerName = trigger.getKey()
            .getName();

        return ProcessedTriggerData.builder()
            .jobName(jobName)
            .triggerName(triggerName)
            .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class ProcessedTriggerData {
        private String jobName;
        private String triggerName;
    }
}
