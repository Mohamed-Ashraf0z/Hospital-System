package com.hospita.sys.features.Logs_AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Logs_AOP {
    private static final Logger logger = LoggerFactory.getLogger(Logs_AOP.class);

    @Around("execution(* com.hospita.sys.features..service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;
        logger.info("{}.{}() started",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());

        logger.info("{}.{}() executed in {} ms",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                executionTime);
        return proceed;
    }
}