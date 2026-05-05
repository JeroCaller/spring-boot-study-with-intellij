package com.jerocaller.AuthTokenSecurity.aop.advices;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAdvice {

    @Around("""
        @annotation(com.jerocaller.AuthTokenSecurity.aop.annotations.LogMethodBoundary) ||
        @within(com.jerocaller.AuthTokenSecurity.aop.annotations.LogMethodBoundary)
    """)
    public Object logMethodBoundary(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("========== {} - {} 실행 시작 ==========", className, methodName);
        Object result = joinPoint.proceed();
        log.info("========== {} - {} 실행 끝 ==========", className, methodName);

        return result;
    }
}
