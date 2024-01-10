package az.company.auth.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import az.company.auth.service.JwtProvider;

@Component
public class CustomAuthorization {

    private final String secret;
    private final HttpServletRequest httpServletRequest;
    private final JwtProvider jwtProvider;

    public CustomAuthorization(@Value("${jwt.secret.access}") String secret, HttpServletRequest httpServletRequest, JwtProvider jwtProvider) {
        this.secret = secret;
        this.httpServletRequest = httpServletRequest;
        this.jwtProvider = jwtProvider;
    }

    public Long getUserIdFromToken() {
        try {

            String authorizationHeader = httpServletRequest.getHeader("Authorization");

            String accessToken = authorizationHeader.substring("Bearer ".length());
            Claims claims = jwtProvider.getAccessClaims(accessToken);


            return Long.parseLong(claims.get("userId").toString());
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Token");
        }
    }

}
