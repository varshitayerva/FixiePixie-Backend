package com.gl.project.BookingService.utility;



import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Matches all methods in the service implementation package
    @Before("execution(* com.gl.project.BookingService.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Executing Service Method: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.gl.project.BookingService.service.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Completed Service Method: {} with result: {}", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "execution(* com.gl.project.BookingService.service.*.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Exception error) {
        logger.error("Exception in Method: {} | Error: {}", joinPoint.getSignature().getName(), error.getMessage());
    }
}


