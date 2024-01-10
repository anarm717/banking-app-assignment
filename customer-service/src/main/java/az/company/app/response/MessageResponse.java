package az.company.app.response;

import az.company.app.errors.SuccessMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class MessageResponse {

    public static ResponseEntity<?> response(SuccessMessage successMessage, Object data, Object error, HttpStatus status) {
        return new ResponseEntity<>(new ResponseModelDTO<>(successMessage.getMessage(), data, error), status);
    }

    public static ResponseEntity<?> response(String message, Object data, Object error, HttpStatus status) {
        return new ResponseEntity<>(new ResponseModelDTO<>(message, data, error), status);
    }

}