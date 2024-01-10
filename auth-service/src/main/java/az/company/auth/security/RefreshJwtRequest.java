package az.company.auth.security;

import lombok.Getter;
import lombok.Setter;

/**

 The RefreshJwtRequest class is a DTO (Data Transfer Object) used for requesting a refreshed JWT (JSON Web Token) from the server.
 It contains the refresh token of the user.
 **/
@Getter
@Setter
public class RefreshJwtRequest {

    public String refreshToken;

}
