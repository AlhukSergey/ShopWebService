package by.teachmeskills.shopwebservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {
    @Pointcut(value = "execution(public * by.teachmeskills.shopwebservice..*(..))")
    public void loggingOperation() {
    }

    @Before(value = "loggingOperation()")
    public void beforeCallMethod(JoinPoint joinPoint) {
        log.info("Calling the method - "
                + joinPoint.getSignature().getName()
                + " of class - "
                + joinPoint.getSourceLocation().getWithinType().getName()
                + ".");
    }

    @AfterReturning(value = "loggingOperation()", returning = "returningValue")
    public void recordSuccessfulExecution(JoinPoint joinPoint, Object returningValue) {
        if (returningValue != null) {
            log.info("The method - "
                    + joinPoint.getSignature().getName()
                    + " of class - "
                    + joinPoint.getSourceLocation().getWithinType().getName()
                    + " executed with results: "
                    + returningValue + ".");
        } else {
            log.info("The method - "
                    + joinPoint.getSignature().getName()
                    + " of class - "
                    + joinPoint.getSourceLocation().getWithinType().getName()
                    + " executed.");
        }
    }

    @AfterThrowing(value = "loggingOperation()", throwing = "exception")
    public void recordFailedExecution(JoinPoint joinPoint, Exception exception) {
        log.error("The method - "
                + joinPoint.getSignature().getName()
                + " of class - "
                + joinPoint.getSourceLocation().getWithinType().getName()
                + " executed with exception: "
                + exception + ".");
    }
}
