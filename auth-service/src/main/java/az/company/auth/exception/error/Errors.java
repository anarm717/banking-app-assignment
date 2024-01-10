package az.company.auth.exception.error;


import org.springframework.http.HttpStatus;

import az.company.auth.exception.ErrorResponse;

public enum Errors implements ErrorResponse {
    USERNAME_NOT_FOUND( "USER_NOT_FOUND", HttpStatus.UNAUTHORIZED , "User with this username not found"),
    INCORRECT_PASSWORD("INCORRECT_PASSWORD", HttpStatus.FORBIDDEN,"Password is incorrect!!!"),
    UNSUPPORTED_JWT("UNSUPPORTED_TOKEN", HttpStatus.FORBIDDEN, "Token is invalid"),
    ROLE_NOT_FOUND("ROLE_NOT_FOUND", HttpStatus.NOT_FOUND, "Role not found!");
    String key;
    HttpStatus httpStatus;
    String message;


    Errors(String key, HttpStatus httpStatus, String message) {
        this.message = message;
        this.key = key;
        this.httpStatus = httpStatus;
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

