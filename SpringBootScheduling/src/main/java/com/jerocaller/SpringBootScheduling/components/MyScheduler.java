package com.jerocaller.SpringBootScheduling.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>Note</p>
 * <p>
 *     {@code @Scheduled} 이용 시 이 어노테이션이 적용된 메서드를 가지고 있는 클래스는
 *     반드시 스프링 빈으로 등록되어야 한다. 따라서 아래 클래스의 경우 {@code @Component}
 *     어노테이션을 부여하였다.
 * </p>
 * <p>
 *     fixedDelay vs fixedRate 비교 테스트 시 둘 중 하나는 {@code @Scheduled} 어노테이션을
 *     주석 처리하여 둘 중 한 메서드만 스케줄링이 되도록 하여 정확한 비교를 가능하게 해야한다.
 * </p>
 * <p>
 *     {@code @Scheduled} 어노테이션이 부여된 메서드는 다음의 조건들을 반드시 지켜야만 한다.
 *     <ol>
 *         <li>스케줄러 메서드는 리턴 타입이 void여야 한다.</li>
 *         <li>스케줄러 메서드에는 매개변수를 정의할 수 없다.</li>
 *     </ol>
 * </p>
 */
@Component
@Slf4j
public class MyScheduler {

    /**
     * <p>
     *     fixedDelay는 이전 작업의 종료 시점부터 정의된 ms 시간만큼 지연된 후
     *     해당 작업을 실행하도록 할 때 쓰인다. 즉, 작업 수행시간을 포함하여 주기적인
     *     시간 간격으로 작업을 반복 실행할 때 쓰인다.
     * </p>
     * <p>
     *     fixedDelay로 정의한 시간보다 작업 수행시간이 더 길어질 경우,
     *     작업 수행 시간이 얼마냐 걸렸느냐에 상관없이 이전 작업이 끝난 후
     *     fixedDelay만큼 지난 후에야 다음 작업이 수행된다.
     *     예) fixedDelay = 1000이고 작업 수행 시간이 2000ms(2초)일 때,
     *     이전 작업이 끝나고 나서 1000ms 뒤에 다음 작업이 수행된다.
     * </p>
     * <p>
     *     즉, 달리 말해 fixedDelay는 이전 작업 수행 시간에 영향을 받지 않고
     *     지정된 시간만큼 지연된 뒤에 실행됨을 보장한다.
     * </p>
     *
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = 1000)
    public void schedulingOne() throws InterruptedException {
        log.info("첫 번째 작업 게시");
        Thread.sleep(2000);  // 이 작업이 2초 걸린다고 가정.
        log.info("첫 번째 작업 끝");
    }

    /**
     * <p>
     *     fixedRate는 이전 작업의 시작 시점으로부터 정의된 ms 시간만큼 지연된 뒤
     *     해당 작업을 다시 실행하도록 할 때 쓰인다.
     * </p>
     * <p>
     *     만약 fixedRate로 지정한 시간보다 작업 수행 시간이 더 길어질 경우,
     *     fixedRate로 지정한 시간 간격은 무시되고 이전 작업 수행 종료 직후에
     *     바로 다음 작업이 수행된다.
     * </p>
     * <p>
     *     예) fixedRate = 1000ms 이고, 작업 수행 시간이 2000ms이면
     *     이전 작업이 2초 걸려 수행을 완료한 뒤, 1000ms를 기다리지 않고
     *     바로 다음 작업이 수행된다.
     * </p>
     * <p>
     *     달리 말하면 fixedRate는 이전 작업 수행 시간에 영향을 받아
     *     만약 작업 수행 시간이 지정된 시간 간격을 넘어가면 지정된 시간 만큼
     *     기다리지 않고 바로 다음 작업을 수행한다. 따라서 주기적인 실행이 보장되지 않는다.
     * </p>
     *
     * @throws InterruptedException
     */
    @Scheduled(fixedRate = 1000)
    public void schedulingTwo() throws InterruptedException {
        log.info("두 번째 작업 개시");
        Thread.sleep(2000);
        log.info("두 번째 작업 끝");
    }
}
