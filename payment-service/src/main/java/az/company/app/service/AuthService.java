package az.company.app.service;

import az.company.app.errors.ErrorsFinal;
import az.company.app.exception.ApplicationException;
import az.company.app.model.JwtAuthentication;
import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**

 AuthService class is responsible for providing authentication and authorization functionality
 to the application. It validates JWT tokens and extracts claims from the tokens.
 It also provides authentication details using the Spring Security context holder.
 */
@Slf4j
@Component
public final class AuthService {

    private final String jwtSecret;

    public AuthService(@Value("${jwt.secret}") String secret) {
        this.jwtSecret = secret;
    }

    /**
     * Validates a given JWT token using the secret key.
     * @param token the JWT token to be validated.
     * @return true if the token is valid, false otherwise.
     * @throws ApplicationException if the token is expired, unsupported, malformed, or invalid.
     */
    public boolean validateToken(String token) throws Exception {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }
         catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
            throw new ApplicationException(ErrorsFinal.EXPIRED_JWT_ERROR);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
            throw new ApplicationException(ErrorsFinal.UNSUPPORTED_JWT_ERROR);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
            throw new ApplicationException(ErrorsFinal.MALFORMED_JWT_ERROR);
        } catch (SignatureException e) {
            throw new ApplicationException(ErrorsFinal.SIGNATURE_JWT_ERROR);
        }
        catch (JwtException e) {
            log.error("invalid token", e);
            throw new ApplicationException(ErrorsFinal.INVALID_TOKEN);
        }
//        return false;
    }


    /**
     * Extracts the claims from the given JWT token.
     * @param token the JWT token.
     * @return the claims extracted from the token.
     */
    public Claims getClaims(@NonNull String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    /**
     * Provides authentication details using the Spring Security context holder.
     * @return the JwtAuthentication object from the SecurityContextHolder.
     */
    public JwtAuthentication getAuthentication() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
