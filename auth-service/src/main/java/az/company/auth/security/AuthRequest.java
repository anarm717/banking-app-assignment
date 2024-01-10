package az.company.auth.security;

import lombok.Getter;
import lombok.Setter;

/**

 The AuthRequest class represents a request object for authentication. It contains a token and a method name.

 */
@Getter
@Setter
public class AuthRequest {

    private String token;
    private String methodName;

}
