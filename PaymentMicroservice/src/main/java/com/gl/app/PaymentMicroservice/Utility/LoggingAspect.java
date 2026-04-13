package com.gl.app.PaymentMicroservice.Utility;

import com.gl.app.PaymentMicroservice.DTO.PaymentDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.gl.app.PaymentMicroservice.Service.PaymentServiceImpl.*(..))")
    public void paymentServiceMethods() {}

    @Before("paymentServiceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();

        if (args.length > 0 && args[0] instanceof PaymentDTO dto) {
            logger.info(">>> [PAYMENT START] Method: {} | BookingID: {} | Amount: {} | Method: {}",
                    methodName, dto.getBookingId(), dto.getAmount(), dto.getPaymentMethod());
        } else {
            logger.info(">>> Entering Method: {}", methodName);
        }
    }

    @AfterReturning(pointcut = "paymentServiceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        logger.info("<<< [PAYMENT SUCCESS] Method: {} completed successfully.",
                joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "paymentServiceMethods()", throwing = "error")
    public void logError(JoinPoint joinPoint, Throwable error) {
        logger.error("!!! [PAYMENT ERROR] Method: {} | Exception: {} | Message: {}",
                joinPoint.getSignature().getName(),
                error.getClass().getSimpleName(),
                error.getMessage());
    }
}