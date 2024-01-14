package az.company.app.errors;

import az.company.app.exception.ErrorResponse;
import org.springframework.http.HttpStatus;

public enum ErrorsFinal implements ErrorResponse {
    DATA_NOT_FOUND( "DATA_NOT_FOUND", HttpStatus.NOT_FOUND, "bu id-li '{id}' '{name}' məlumat tapılmadı"),
    DATA_NOT_FOUND_LAST( "DATA_NOT_FOUND_LAST", HttpStatus.NOT_FOUND, "{message}"),
    BAD_REQUEST( "BAD_REQUEST", HttpStatus.BAD_REQUEST, "daxil edilən parametr(lər) yanlışdır: {message}"),
    INTERNAL_SERVER_ERROR( "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "daxili server xətası"),
    SERVICE_TYPE_ERROR("SERVICE_TYPE_ERROR", HttpStatus.BAD_REQUEST, "{message}"),
    PARAM_IN_USE("PARAM_IN_USE", HttpStatus.BAD_REQUEST, "parametrin istifadə edildiyi dəyər mövcuddur"),

    //pdf generation exception messages
    GENERATE_PDF_EXCEPTION( "GENERATE_PDF_EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR, "{message}"),
    
    TRANSACTION_NOT_FOUND( "TRANSACTION_NOT_FOUND", HttpStatus.NOT_FOUND, "transaction not found : {transactionId}"),
    MULTIPLE_TRANSACTION_FOUND( "MULTIPLE_TRANSACTION_FOUND", HttpStatus.NOT_FOUND, "multiple transactions not found : {transactionId}"),
    WRONG_REFUND_AMOUNT( "WRONG_REFUND_AMOUNT", HttpStatus.BAD_REQUEST, "Refund amount higher than purchase amount"),
    
    GSM_NUMBER_NOT_FOUND( "GSM_NUMBER_NOT_FOUND", HttpStatus.NOT_FOUND, "GSM Number not found : '{gsmNumber}'"),
    CUSTOMER_SERVICE_ERROR( "CUSTOMER_SERVICE_ERROR", HttpStatus.CONFLICT, "Customer Service Error"),
    WRONG_AMOUNT( "WRONG_AMOUNT", HttpStatus.BAD_REQUEST, "Wrong amount"),
    //for testing exceptions
    EXCEPTION_IN_TESTING("EXCEPTION_IN_TESTING", HttpStatus.INTERNAL_SERVER_ERROR, "testing zamanı xəta yarandı"),

    //security exception messages
//    JWT_ERROR("JWT_ERROR",HttpStatus.UNAUTHORIZED,"{message}"),
    ACCESS_DENIED("ACCESS_DENIED",HttpStatus.FORBIDDEN,"İcazə yoxdur"),
    EXPIRED_JWT_ERROR("EXPIRED_JWT_ERROR", HttpStatus.UNAUTHORIZED, "JWT token-in vaxtı keçib"),
    UNSUPPORTED_JWT_ERROR("UNSUPPORTED_JWT_ERROR",HttpStatus.UNAUTHORIZED,"Bu token formatı dəstəklənmir"),
    MALFORMED_JWT_ERROR("MALFORMED_JWT_ERROR",HttpStatus.UNAUTHORIZED,"Token düzgün formatda deyil"),
    SIGNATURE_JWT_ERROR("SIGNATURE_JWT_ERROR",HttpStatus.UNAUTHORIZED,"Token-in signature - i səhvdi"),
    INVALID_TOKEN("INVALID_TOKEN",HttpStatus.UNAUTHORIZED,"Token səhvdi"),

    //for standard exception messages
    UNIQUE_CONSTRAINT( "23505", HttpStatus.BAD_REQUEST, "təkrarlana bilməz"),
    FK_CONSTRAINT( "23503", HttpStatus.BAD_REQUEST, "əlaqəli olduğu məlumat xətası"),
    NOT_EMPTY_CONSTRAINT( "23502", HttpStatus.BAD_REQUEST, "boş ola bilməz"),
    CHECK_VIOALATION( "23514", HttpStatus.BAD_REQUEST, "check violation xətası");

    final String key;
    final HttpStatus httpStatus;
    final String message;

    ErrorsFinal(String key, HttpStatus httpStatus, String message) {
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
