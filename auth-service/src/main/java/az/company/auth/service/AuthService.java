package az.company.auth.service;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import az.company.auth.dto.UserDto.UserDto;
import az.company.auth.entity.*;
import az.company.auth.exception.ApplicationException;
import az.company.auth.exception.error.Errors;
import az.company.auth.repository.*;
import az.company.auth.security.AuthRequest;
import az.company.auth.security.JwtAuthentication;
import az.company.auth.security.JwtRequest;
import az.company.auth.security.JwtResponse;
import az.company.auth.service.users.UserService;

import java.util.*;
import java.util.stream.Collectors;

/**

 This package contains the AuthService class that provides methods for user authentication and authorization.
 The AuthService class provides the following methods:
 login(): Generates an access token and a refresh token for a user based on their username and password.
 getAccessToken(): Generates a new access token based on a refresh token.
 refresh(): Generates a new refresh token and access token based on a refresh token.
 getAuthInfo(): Returns the authentication information of the current user.
 isAuthorized(): Checks whether the user is authorized to perform the specified operation.
 This package also contains the following classes:
 JwtRequest: Represents a request to generate a JWT token.
 JwtResponse: Represents a response containing a JWT token.
 JwtAuthentication: Represents the authentication information of a user.

 */

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserJWTService userJWTService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final HttpServletRequest httpServletRequest;
    private final UserPermissionRepository userPermissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRoleRopository userRoleRopository;
    private final ObjectEntityRepository objectEntityRepository;
    /**

     Attempts to log in the user with the given authentication request, and returns a JWT response with the

     user's access and refresh tokens if successful.

     @param authRequest the JwtRequest containing the user's authentication information

     @return the JwtResponse containing the user's access and refresh tokens if authentication is successful

     @throws ApplicationException if the user is not found or the password is incorrect
     */
    public JwtResponse login(@NonNull JwtRequest authRequest) {
        final User user = userJWTService.getByUsername(authRequest.getUsername())
                .orElseThrow(() -> new ApplicationException(Errors.USERNAME_NOT_FOUND));

        if (user.getStatus()=='1'&&new BCryptPasswordEncoder().matches(authRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getUsername(), refreshToken);
            return new JwtResponse(user.getId(),accessToken, refreshToken);
        } else {
            throw new ApplicationException(Errors.INCORRECT_PASSWORD);
        }
    }

    public String getUsernameFromToken(){
        try {

            String authorizationHeader = httpServletRequest.getHeader("Authorization");

            String accessToken = authorizationHeader.substring("Bearer ".length());
            Claims claims = jwtProvider.getAccessClaims(accessToken);

            Long userId =  Long.parseLong(claims.get("userId").toString());

            String username = userService.getUsernameByUserId(userId);
            return username;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Token");
        }
    }

    /**
     Generates an access token for the given refresh token. If the refresh token is valid and corresponds to a known user,
     a new access token is generated and returned along with a new refresh token. The old refresh token is removed from the
     refresh storage and the new refresh token is added. If the refresh token is invalid or does not correspond to a known user,
     an AuthException is thrown.
     @param refreshToken the refresh token for which to generate a new access token
     @return a JwtResponse containing the new access token and the new refresh token
     @throws AuthException if the refresh token is invalid or does not correspond to a known user
     */
    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userJWTService.getByUsername(username)
                        .orElseThrow(() -> new AuthException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.remove(user.getUsername());
                refreshStorage.put(user.getUsername(), refreshToken);
                return new JwtResponse(user.getId(),accessToken,null);
            }
        }
        throw new AuthException("Invalid JWT token");
    }

    /**

     Refreshes a user's refresh token using their old refresh token.

     @param refreshToken the user's refresh token

     @return a {@link JwtResponse} containing the user's  access token which is null at this moment and refresh token

     @throws AuthException if the provided refresh token is invalid or does not match the saved refresh token

     */

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userJWTService.getByUsername(username)
                        .orElseThrow(() -> new AuthException("User not found"));

                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JwtResponse(user.getId(),null,newRefreshToken);
            }
        }
        throw new AuthException("Invalid JWT token");
    }

    /**

     Retrieves the authentication information of the current security context.
     @return the authentication information in the form of a JwtAuthentication object.
     */
    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }


    /**

     Checks if the authenticated user has access to the requested method.

     @param authRequest the authorization request containing the method name and JWT token.

     @return true if the user has access to the method, false otherwise.
     */
    public Boolean isAuthorized(AuthRequest authRequest){
        String token = authRequest.getToken().replace("Bearer ", "");
        Claims claims = jwtProvider.getClaims(token,jwtProvider.getJwtAccessSecret());
        Integer userIdInt = (Integer) claims.get("userId");
        Long userId = userIdInt.longValue();


        List<UserPermission> userPermissions = userPermissionRepository.findByUserId(userId);
        List<UserRole> userRoles = userRoleRopository.findByUserId(userId);

        for (UserRole userRole :userRoles){
            if (userRole.getRole().getRoleName().equals(authRequest.getMethodName())) {
                return true;
            }
        } 

        Set<Permission> permissions = userPermissions.stream()
                .map(UserPermission::getPermission)
                .collect(Collectors.toSet());


        userRoles.stream()
                .map(UserRole::getRole)
                .flatMap(role -> rolePermissionRepository.findByRoleId(role.getId()).stream())
                .map(RolePermission::getPermission)
                .forEach(permissions::add);


        List<ObjectEntity> objectEntity = objectEntityRepository.findByObjectIdNameAndObjectTypeId(authRequest.getMethodName(), 4L);

        if (objectEntity==null||objectEntity.isEmpty()) {
            return false;
        }

        Set<Permission> objectPermissions = objectEntity.stream()
                .map(ObjectEntity::getPermissionId)
                .collect(Collectors.toSet());


        return permissions.stream().anyMatch(objectPermissions::contains);

    }

}
