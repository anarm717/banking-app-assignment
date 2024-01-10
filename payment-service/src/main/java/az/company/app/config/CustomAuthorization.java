package az.company.app.config;


import az.company.app.errors.ErrorsFinal;
import az.company.app.exception.ApplicationException;
import az.company.app.model.AuthorizeRequest;
import az.company.app.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**

 This class provides functionality for custom authorization using JSON Web Token (JWT).
 The class takes a secret and HTTPServletRequest object as constructor parameters.
 The isValid method is used to check if a method is authorized or not using the received JSON Web Token.
 If the method is authorized, it returns true, otherwise it throws an ApplicationException with the ACCESS_DENIED message.
 The method uses a RestTemplate to send a POST request with the JWT to the authorization endpoint of the server.
 The response from the server is then used to determine whether the method is authorized or not.

 */

@Component
public class CustomAuthorization {

    private final String secret;
    private final HttpServletRequest httpServletRequest;
    private final AuthService authService;
    private final String authUrl;
    /**

     Constructs a new CustomAuthorization object with the given secret key and HTTP servlet request.
     * @param secret The secret key used for JWT authentication.
     * @param httpServletRequest The HTTP servlet request used to retrieve the authorization header.
     * @param authService
     * @param authUrl
     */
    public CustomAuthorization(@Value("${jwt.secret}") String secret, HttpServletRequest httpServletRequest, AuthService authService,
                               @Value("${auth.url}") String authUrl) {
        this.secret = secret;
        this.httpServletRequest = httpServletRequest;
        this.authService = authService;
        this.authUrl = authUrl;
    }

    public Long getUserIdFromToken() {
        try {

            String authorizationHeader = httpServletRequest.getHeader("Authorization");

            String accessToken = authorizationHeader.substring("Bearer ".length());
            Claims claims = authService.getClaims(accessToken);


            return Long.parseLong(claims.get("userId").toString());
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Token");
        }
    }

    public Long getEmployeeIdFromToken() {
        try {

            String authorizationHeader = httpServletRequest.getHeader("Authorization");

            String accessToken = authorizationHeader.substring("Bearer ".length());
            Claims claims = authService.getClaims(accessToken);


            return Long.parseLong(claims.get("employeeId").toString());
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Token");
        }
    }
    
    /**

     Checks if the user is authorized to access a certain method by sending an authorization request to an external API.
     @param methodName a String representing the name of the method to be checked for authorization
     @return a boolean indicating whether the user is authorized to access the method or not
     @throws JsonProcessingException if there is an error while processing JSON data
     @throws ApplicationException if the user is not authorized to access the method
     */
    public boolean isValid(String methodName) throws JsonProcessingException {


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String accessToken = authorizationHeader.substring("Bearer ".length());

        AuthorizeRequest jwtRequest = new AuthorizeRequest(accessToken,methodName);

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(jwtRequest);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                authUrl,
                HttpMethod.POST,
                requestEntity,
                Boolean.class
        );

        boolean authorized = Boolean.TRUE.equals(response.getBody());
        if (!authorized) {
            throw new ApplicationException(ErrorsFinal.ACCESS_DENIED);
        }

        return authorized;

    }
    public ArrayList<String> getRolesFromToken() {
        try {
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String accessToken = authorizationHeader.substring("Bearer ".length());
            Claims claims = authService.getClaims(accessToken);
            String roles = claims.get("roles").toString();
            roles = roles.replace('=', ':');
            JSONArray jsonArray = new JSONArray(roles);
            ArrayList<String> arList=new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                arList.add(explrObject.get("roleName").toString());
            }
            return arList;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Token");
        }
    }

}
