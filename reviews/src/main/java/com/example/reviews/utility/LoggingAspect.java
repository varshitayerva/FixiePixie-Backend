package com.example.reviews.utility;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.reviews.controller..*(..)) || execution(* com.example.reviews.service..*(..))")
    public Object logApplicationMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        String arguments = Arrays.toString(joinPoint.getArgs());

        logger.info("Entering {} args={}", methodName, arguments);
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Exiting {} result={} durationMs={}", methodName, result, duration);
            return result;
        } catch (Throwable exception) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Exception in {} args={} durationMs={} message={}",
                    methodName, arguments, duration, exception.getMessage(), exception);
            throw exception;
        }
    }
}
