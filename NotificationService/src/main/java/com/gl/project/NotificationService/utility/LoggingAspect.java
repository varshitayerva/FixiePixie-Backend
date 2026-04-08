package com.gl.project.NotificationService.utility;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger =
            LogManager.getLogger(LoggingAspect.class);

    @Before("execution(* com.gl.project.NotificationService.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entering Method: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(
            pointcut = "execution(* com.gl.project.NotificationService.service.*.*(..))",
            returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Exiting Method: {}", joinPoint.getSignature().getName());
        logger.info("Returned Value: {}", result);
    }

    @AfterThrowing(
            pointcut = "execution(* com.gl.project.NotificationService.service.*.*(..))",
            throwing = "ex"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        logger.error("Exception in Method: {}", joinPoint.getSignature().getName());
        logger.error("Error: {}", ex.getMessage());
    }
}