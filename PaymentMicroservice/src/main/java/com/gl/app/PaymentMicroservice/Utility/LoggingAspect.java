package com.gl.app.PaymentMicroservice.Utility;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.gl.app.PaymentMicroservice.Service..*(..))")
    public Object logServiceCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        log.info("Entering {} with args {}", method, Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("Exiting {} with result {}", method, result);
            return result;
        } catch (Throwable ex) {
            log.error("Exception in {}: {}", method, ex.getMessage(), ex);
            throw ex;
        }
    }

    @AfterThrowing(pointcut = "execution(* com.gl.app.PaymentMicroservice.Controller..*(..))", throwing = "ex")
    public void logControllerException(JoinPoint joinPoint, Throwable ex) {
        log.error("Controller exception in {}: {}", joinPoint.getSignature().toShortString(), ex.getMessage(), ex);
    }
}
