package az.company.auth.service;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import az.company.auth.entity.Role;
import az.company.auth.security.JwtAuthentication;

/**

 JwtUtils provides utility methods for generating JwtAuthentication object from the JWT Claims object.
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    /**
     * Generates a JwtAuthentication object from the given JWT Claims object.
     *
     * @param claims the JWT Claims object
     * @return a JwtAuthentication object
     */
    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    /**
     * Extracts and returns the set of Roles from the given JWT Claims object.
     *
     * @param claims the JWT Claims object
     * @return a set of Roles
     */
    private static Set<Role> getRoles(Claims claims) {

        final List roles = claims.get("roles", List.class);
        final Set<Role> roleSet = new HashSet<>();


        for (Object o : roles) {

            LinkedHashMap<String, String> l = (LinkedHashMap<String, String>) o;
            Role r = new Role();

            r.setRoleName(l.get("roleName"));
            roleSet.add(r);
        }

        return roleSet;

    }

}
