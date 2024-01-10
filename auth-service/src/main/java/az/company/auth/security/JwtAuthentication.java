package az.company.auth.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import az.company.auth.entity.Role;

import java.util.Collection;
import java.util.Set;

/**

 This class represents the authentication information extracted from a JWT token.

 It implements the Spring Security Authentication interface to integrate with the security framework.
 */

@Getter
@Setter
public class JwtAuthentication implements Authentication {

    private boolean authenticated;
    private String username;
    private String firstName;
    private Set<Role> roles;


    /**

     @return the authorization role(s) of the authenticated user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return roles; }

    /**

     Returns the credentials of the authenticated user.
     @return null since credentials are not stored in this implementation
     */
    @Override
    public Object getCredentials() { return null; }

    /**

     Returns additional details about the authenticated user.
     @return null since additional details are not stored in this implementation
     */
    @Override
    public Object getDetails() { return null; }

    /**

     @return the principal (username) of the authenticated user
     */
    @Override
    public Object getPrincipal() { return username; }

    /**

     @return true if the user is authenticated, false otherwise
     */
    @Override
    public boolean isAuthenticated() { return authenticated; }

    /**

     Sets whether the user is authenticated or not.
     @param isAuthenticated true if the user is authenticated, false otherwise
     @throws IllegalArgumentException if the argument is null or invalid
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    /**

     @return the first name of the authenticated user
     */
    @Override
    public String getName() { return firstName; }

}