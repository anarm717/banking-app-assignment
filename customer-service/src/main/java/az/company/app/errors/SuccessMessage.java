package az.company.app.errors;

import az.company.app.exception.SuccessResponse;
import org.springframework.http.HttpStatus;

public enum SuccessMessage implements SuccessResponse {
    SUCCESS_ADD( "SUCCESS_ADD", HttpStatus.CREATED, "Məlumat uğurla əlavə edildi!"),
    SUCCESS_DELETE( "SUCCESS_DELETE", HttpStatus.OK, "Məlumat uğurla silindi!"),
    SUCCESS_GET( "SUCCESS_GET", HttpStatus.OK, "Məlumat uğurla əldə edildi!"),
    SUCCESS_UPDATE( "SUCCESS_UPDATE", HttpStatus.OK, "Məlumat uğurla dəyişdirildi!");

    final String key;
    final HttpStatus httpStatus;
    final String message;

    SuccessMessage(String key, HttpStatus httpStatus, String message) {
        this.key = key;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
