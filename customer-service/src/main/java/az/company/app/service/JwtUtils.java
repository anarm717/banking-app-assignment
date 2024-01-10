package az.company.app.service;

import az.company.app.model.JwtAuthentication;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**

 The JwtUtils class provides static methods for generating a JwtAuthentication object from a set of claims
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    /**
     * Generates a JwtAuthentication object from a set of claims
     * @param claims - the claims used to generate the JwtAuthentication object
     * @return - the generated JwtAuthentication object
     */
    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getPermissions(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    /**
     * Generates a set of GrantedAuthority objects from a set of claims
     * @param claims - the claims used to generate the set of GrantedAuthority objects
     * @return - the generated set of GrantedAuthority objects
     */
    private static Set<GrantedAuthority> getPermissions(Claims claims) {
        final List roles = claims.get("roles", List.class);
        final Set<GrantedAuthority> rolesSet = new HashSet<>();

        for (Object o : roles) {
            LinkedHashMap<String, String> l = (LinkedHashMap<String, String>) o;
            rolesSet.add((GrantedAuthority) () -> l.get("roleName"));
        }
        return rolesSet;
    }

}
