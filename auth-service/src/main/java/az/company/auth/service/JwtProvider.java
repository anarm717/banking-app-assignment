package az.company.auth.service;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import az.company.auth.converter.RoleMapper;
import az.company.auth.dto.RoleDto.RolesGetDto;
import az.company.auth.entity.Role;
import az.company.auth.entity.User;
import az.company.auth.entity.UserRole;
import az.company.auth.exception.ApplicationException;
import az.company.auth.exception.error.Errors;
import az.company.auth.repository.UserRoleRopository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**

 The JwtProvider class generates and validates JSON Web Tokens (JWTs) for user authentication.
 It uses two different secret keys for generating access and refresh tokens and also provides methods for validating and retrieving the claims from a given token.
 */
@Slf4j
@Component
@Getter
//@RequiredArgsConstructor
public class JwtProvider {

    private final String jwtAccessSecret;
    private final String jwtRefreshSecret;
    private final RoleMapper rolesConverter;

    private final UserRoleRopository userRoleRopository;

    /**
     * Constructor for JwtProvider class.
     *
     * @param jwtAccessSecret the secret key used for generating access tokens
     * @param jwtRefreshSecret the secret key used for generating refresh tokens
     * @param rolesConverter the converter used for mapping Role entities to RoleBaseDto objects
     * @param userRoleRopository the repository for performing database operations on UserRole entities
     */
    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
            RoleMapper rolesConverter, UserRoleRopository userRoleRopository) {
        this.jwtAccessSecret = jwtAccessSecret;
        this.jwtRefreshSecret = jwtRefreshSecret;
        this.rolesConverter = rolesConverter;
        this.userRoleRopository = userRoleRopository;
    }

    /**
     * Generates a new access token for a given user.
     *
     * @param user the user for whom the token is being generated
     * @return the generated access token as a string
     */
    public String generateAccessToken(@NonNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusDays(1).atZone(ZoneId.systemDefault()).toInstant();
//        final Instant accessExpirationInstant = now.plusSeconds(20).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        List<RolesGetDto> rolesDto = new ArrayList<>();

        List<Role> roles = new ArrayList<>();

        for(UserRole u : user.getUserRoles()){
            roles.add(u.getRole());
        }

        for (Role r : roles){
            rolesDto.add(rolesConverter.roleToDto(r));

        }


        return Jwts.builder()
//                .setId(String.valueOf(user.getId()))
                .setSubject(user.getUsername())
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS512, jwtAccessSecret)
                .claim("userId", user.getId())
                .claim("employeeId", user.getEmployeeId() )
                .claim("roles", rolesDto)
                .compact();
    }


    /**

     Generates a new refresh token for the given user. The refresh token is valid for 30 days from the current date and time.
     @param user The user for whom to generate the refresh token.
     @return The generated refresh token.
     @throws NullPointerException If the user parameter is null.
     */
    public String generateRefreshToken(@NonNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
//        final Instant refreshExpirationInstant = now.plusSeconds(3).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS512, jwtRefreshSecret)
                .compact();
    }

    /**

     Validates the given access token using the JWT Access Secret.
     @param token the access token to be validated
     @return true if the access token is valid, false otherwise
     @throws NullPointerException if the token is null
     */
    public boolean validateAccessToken(@NonNull String token) {
        return validateToken(token, jwtAccessSecret);
    }

    /**

     Validates the given refresh token using the JWT Refresh Secret.
     @param token the refresh token to be validated
     @return true if the refresh token is valid, false otherwise
     @throws NullPointerException if the token is null
     */
    public boolean validateRefreshToken(@NonNull String token) {
        return validateToken(token, jwtRefreshSecret);
    }

    /**

     Validates the given JWT token using the provided secret.
     @param token the JWT token to be validated
     @param secret the secret key used to sign the JWT token
     @return true if the token is valid, false otherwise
     @throws ApplicationException if the token is malformed
     @throws NullPointerException if either token or secret is null
     */
    boolean validateToken(@NonNull String token, @NonNull String secret) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
            throw new ApplicationException(Errors.UNSUPPORTED_JWT);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    /**

     Returns the claims from the access token using the JWT Access Secret.
     @param token the access token from which to extract the claims
     @return the claims extracted from the access token
     @throws NullPointerException if the token is null
     */
    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    /**

     Returns the claims from the refresh token using the JWT Refresh Secret.
     @param token the refresh token from which to extract the claims
     @return the claims extracted from the refresh token
     @throws NullPointerException if the token is null
     */
    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }


    /**

     Extracts the claims from the given JWT token using the provided secret.
     @param token the JWT token from which to extract the claims
     @param secret the secret key used to sign the JWT token
     @return the claims extracted from the JWT token
     @throws NullPointerException if either token or secret is null
     */
    public Claims getClaims(@NonNull String token, @NonNull String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

}
