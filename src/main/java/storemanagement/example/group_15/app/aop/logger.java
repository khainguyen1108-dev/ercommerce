package storemanagement.example.group_15.app.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class logger {
    private static final Logger log = LoggerFactory.getLogger(logger.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {
    }

    @Before("restControllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String controllerMethod = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("[REQUEST] {} {} -> {} with args {}", method, uri, controllerMethod, Arrays.toString(args));
    }

    @AfterReturning(pointcut = "restControllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[RESPONSE] {} returned with value: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(pointcut = "restControllerMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("[EXCEPTION] {} threw exception: {}", joinPoint.getSignature(), ex.getMessage());
    }
}
