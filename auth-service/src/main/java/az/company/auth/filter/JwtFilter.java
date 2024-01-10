package az.company.auth.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import az.company.auth.security.JwtAuthentication;
import az.company.auth.service.JwtProvider;
import az.company.auth.service.JwtUtils;

import java.io.IOException;

/**

 The JwtFilter class is responsible for filtering and processing JWT tokens.
 It extends the GenericFilterBean class and implements the doFilter() method to filter requests and responses.
 The filter extracts the JWT token from the HTTP request's authorization header and validates it using the JwtProvider service.
 If the token is valid, it extracts the token's claims using the same service and generates a JwtAuthentication object using JwtUtils.
 Finally, it sets the JwtAuthentication object as the authenticated principal using the SecurityContextHolder.

 **/

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";


    private final JwtProvider jwtProvider;


    /**
     * Filters the HTTP request and response and processes the JWT token.
     *
     * @param request the servlet request to be filtered
     * @param response the servlet response to be filtered
     * @param fc the filter chain to execute
     * @throws IOException if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && jwtProvider.validateAccessToken(token)) {
            final Claims claims = jwtProvider.getAccessClaims(token);
            final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }

        fc.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the authorization header of the HTTP request.
     *
     * @param request the HTTP request to extract the token from
     * @return the JWT token, or null if it cannot be extracted
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}