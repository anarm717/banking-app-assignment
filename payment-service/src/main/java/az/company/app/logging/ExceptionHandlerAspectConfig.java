package az.company.app.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ExceptionHandlerAspectConfig {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAspectConfig.class);

    @Autowired
    private ObjectMapper mapper;

    @Pointcut("execution(* az.company.app.exception.GlobalExceptionHandler.*(..)) ")
    public void pointcut() {}

    @AfterReturning(pointcut = "pointcut()", returning = "entity")
    public void logMethodAfter(JoinPoint joinPoint, ResponseEntity<?> entity) {
        try {
            logger.info("   <=== returning: {}",
                    mapper.writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }
    }

}
