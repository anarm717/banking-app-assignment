package az.company.auth.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import az.company.auth.security.AuthRequest;
import az.company.auth.security.JwtRequest;
import az.company.auth.security.JwtResponse;
import az.company.auth.security.RefreshJwtRequest;
import az.company.auth.service.AuthService;

/**
 * The AuthController class handles authentication-related HTTP requests.
 */
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    private final AuthService authService;

    /**
     * This endpoint processes a login request and returns a JWT token if authentication succeeds.
     *
     * @param authRequest The authentication request.
     * @return The JWT token in a JwtResponse object.
     */
    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    /**
     * This endpoint generates a new access token using a refresh token.
     *
     * @param request The refresh token request.
     * @return The new JWT access token in a JwtResponse object.
     * @throws AuthException If the refresh token is invalid or expired.
     */
    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    /**
     * This endpoint generates a new refresh token using a valid refresh token.
     *
     * @param request The refresh token request.
     * @return The new JWT refresh token in a JwtResponse object.
     * @throws AuthException If the refresh token is invalid or expired.
     */
    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    /**
     * This endpoint checks if a user is authorized to access a resource.
     *
     * @param authRequest The authorization request.
     * @return True if the user is authorized, false otherwise.
     */
    @PostMapping("/authorize")
    public ResponseEntity<?> getAnswer(@RequestBody AuthRequest authRequest){

        return ResponseEntity.ok(authService.isAuthorized(authRequest));

    }

    @GetMapping("/getUsernameFromToken")
    public ResponseEntity<?> getUsernameFromToken(){
        return ResponseEntity.ok(authService.getUsernameFromToken());
    }

}
