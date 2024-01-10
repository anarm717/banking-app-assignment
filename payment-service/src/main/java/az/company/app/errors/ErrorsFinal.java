package az.company.app.errors;

import az.company.app.exception.ErrorResponse;
import org.springframework.http.HttpStatus;

public enum ErrorsFinal implements ErrorResponse {
    CANT_REMOVE_DIN("CANT_REMOVE_DIN", HttpStatus.CONFLICT, "Nazirliyin ləğvi və ya silinməsi qadağandır!!!"),
    DATA_NOT_FOUND( "DATA_NOT_FOUND", HttpStatus.NOT_FOUND, "bu id-li '{id}' '{name}' məlumat tapılmadı"),
    DATA_NOT_FOUND_LAST( "DATA_NOT_FOUND_LAST", HttpStatus.NOT_FOUND, "{message}"),
    STAFF_UNIT_BAD_REQUEST( "STAFF_UNIT_BAD_REQUEST", HttpStatus.BAD_REQUEST, "daxil edilən parametr(lər) yanlışdır: {message}"),
    INTERNAL_SERVER_ERROR( "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "daxili server xətası"),
    HAS_CHILD_ERROR("HAS_CHILD_ERROR", HttpStatus.METHOD_NOT_ALLOWED, "strukturun alt strukturları olduğu üçün silmək qadağandır!!!"),
    STAFF_ORDER_NOS_IS_NOT_EQUAL("STAFF_ORDER_NOS_IS_NOT_EQUAL", HttpStatus.BAD_REQUEST, "ştatların sıra nömrəsi düzgün deyil"),
    MAIN_STAFF_UNIT_ERROR("MAIN_STAFF_UNIT_ERROR", HttpStatus.BAD_REQUEST, "{message}"),
    SERVICE_TYPE_ERROR("SERVICE_TYPE_ERROR", HttpStatus.BAD_REQUEST, "{message}"),
    CHILD_HAS_STAFF("CHILD_HAS_STAFF", HttpStatus.BAD_REQUEST, "{name} bağlı olduğu ştat olduğu üçün ilk öncə ştatlarda dəyişiklik edin!!!"),
    CHECK_EMPLOYEE_DURING_DELETE_STAFF("CHECK_EMPLOYEE_DURING_DELETE_STAFF", HttpStatus.BAD_REQUEST, "ştata bağlı işçilər var"),
    CHECK_EMPLOYEE_DELETE_NOT_ENOUGH_STAFF("CHECK_EMPLOYEE_DELETE_NOT_ENOUGH_STAFF", HttpStatus.BAD_REQUEST, "yetərincə işçisi boş ştat yoxdur"),
    STAFF_PARAM_NAME_IN_USE("STAFF_PARAM_NAME_IN_USE", HttpStatus.BAD_REQUEST, "parametrin istifadə edildiyi dəyər mövcuddur"),

    //pdf generation exception messages
    GENERATE_PDF_EXCEPTION( "GENERATE_PDF_EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR, "{message}"),

    TRANSACTION_NOT_FOUND( "TRANSACTION_NOT_FOUND", HttpStatus.NOT_FOUND, "transaction not found : {transactionId}"),
    MULTIPLE_TRANSACTION_FOUND( "MULTIPLE_TRANSACTION_FOUND", HttpStatus.NOT_FOUND, "multiple transactions not found : {transactionId}"),
    

    //get staff unit attributes
    STAFF_UNIT_GET_ATTRIBUTES("STAFF_UNIT_GET_ATTRIBUTES", HttpStatus.INTERNAL_SERVER_ERROR, "{message}"),

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
