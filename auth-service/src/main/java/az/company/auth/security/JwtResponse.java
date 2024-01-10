package az.company.auth.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**

 The JwtResponse class represents the response object that is returned after successful authentication.
 The response contains a Bearer token, user ID, access token and refresh token.
 */
@Getter
@AllArgsConstructor
public class JwtResponse {

    private final String type = "Bearer";
    private long userId;
    private String accessToken;
    private String refreshToken;

}
