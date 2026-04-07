package com.gl.project.ProviderService.utility;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.gl.project.ProviderService.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println(" Entering Method: " + joinPoint.getSignature().getName());
    }


    @AfterReturning(pointcut = "execution(* com.gl.project.ProviderService.service.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println(" Exiting Method: " + joinPoint.getSignature().getName());
        System.out.println(" Returned Value: " + result);
    }


    @AfterThrowing(pointcut = "execution(* com.gl.project.ProviderService.service.*.*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        System.out.println(" Exception in Method: " + joinPoint.getSignature().getName());
        System.out.println(" Error: " + ex.getMessage());
    }
}