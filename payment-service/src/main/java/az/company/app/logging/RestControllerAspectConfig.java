package az.company.app.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class RestControllerAspectConfig {


    private static final Logger logger = LoggerFactory.getLogger(RestControllerAspectConfig.class);

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired
    private ObjectMapper mapper;

    @Pointcut("within(az.company.app.controller..*) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void pointcut() {}

    @Before("pointcut()")
    public void logMethodBefore(JoinPoint joinPoint) {
        before(joinPoint, findMapping(joinPoint));
    }

    @AfterReturning(pointcut = "pointcut()", returning = "entity")
    public void logMethodAfter(JoinPoint joinPoint, ResponseEntity<?> entity) {
        after(joinPoint, entity, findMapping(joinPoint));
    }

    private <T extends Annotation> void before(JoinPoint joinPoint, Class<T> cl){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method ms = signature.getMethod();
        T mapping  = ms.getAnnotation(cl);
        Map<String, Object> parameters = getParameters(joinPoint);

        try {
            logger.info("   ===> method: {}, path: {}, type: {}, arguments: {} ",
                    ms.getName(),
                    request.getRequestURI(),
                    mapping.annotationType().getSimpleName() ,
                    mapper.writeValueAsString(parameters));
        } catch (JsonProcessingException e) {
            logger.error("Error while converting -> {}", e.getMessage());
        }
    }

    private <T extends Annotation> void after(JoinPoint joinPoint, ResponseEntity<?> entity, Class<T> cl){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method ms = signature.getMethod();
        T mapping  = signature.getMethod().getAnnotation(cl);

        try {
            logger.info("   <=== method: {}, path: {}, type: {}, status: {}",
                    ms.getName(),
                    request.getRequestURI(),
                    mapping.annotationType().getSimpleName(),
                    entity.getStatusCode());
        } catch (/*JsonProcessingException*/Exception e) {
            logger.error("Error while converting -> {}", e.getMessage());
        }
    }

    private Class<? extends Annotation> findMapping(JoinPoint joinPoint){
        Class<? extends Annotation> cl = null;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if(signature.getMethod().getAnnotation(GetMapping.class) != null){
            cl = GetMapping.class;
        }
        if(signature.getMethod().getAnnotation(PutMapping.class) != null){
            cl = PutMapping.class;
        }
        if(signature.getMethod().getAnnotation(PostMapping.class) != null){
            cl = PostMapping.class;
        }
        if(signature.getMethod().getAnnotation(DeleteMapping.class) != null){
            cl = DeleteMapping.class;
        }
        if(signature.getMethod().getAnnotation(PatchMapping.class) != null){
            cl = PatchMapping.class;
        }
        return cl;
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        HashMap<String, Object> map = new HashMap<>();

        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }

        return map;
    }

}
