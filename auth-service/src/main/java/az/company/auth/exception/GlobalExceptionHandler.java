package az.company.auth.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends DefaultErrorAttributes {

    private final MessageSource messageSource;


    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Map<String, Object>> handle(ApplicationException ex,
                                                      WebRequest request) {
        return ofType(request, ex.getErrorResponse().getHttpStatus(), ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Map<String, Object>> handle(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        List<ConstraintsViolationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ConstraintsViolationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        String localizedMessage = messageSource.getMessage(ex.getClass().getName().concat(".message"),
                new Object[]{}, LocaleContextHolder.getLocale());
        return ofType(request, HttpStatus.BAD_REQUEST, localizedMessage, null, validationErrors);
    }

    protected ResponseEntity<Map<String, Object>> ofType(WebRequest request,
                                                         HttpStatus status, ApplicationException ex) {
        return ofType(request, status, ex.getLocalizedMessage(LocaleContextHolder.getLocale(), messageSource),
                ex.getErrorResponse().getKey(), Collections.EMPTY_LIST);
    }

    private ResponseEntity<Map<String, Object>> ofType(WebRequest request, HttpStatus status, String message,
                                                       String key, List validationErrors) {
        Map<String, Object> attributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        attributes.put(HttpResponseConstants.STATUS, status.value());
        attributes.put(HttpResponseConstants.ERROR, status);
        attributes.put(HttpResponseConstants.MESSAGE, message);
        attributes.put(HttpResponseConstants.ERRORS, validationErrors);
        attributes.put(HttpResponseConstants.ERROR_KEY, key);
        attributes.put(HttpResponseConstants.PATH, ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(attributes, status);
    }


}
