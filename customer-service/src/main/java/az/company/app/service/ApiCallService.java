package az.company.app.service;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface ApiCallService {

    <T> ResponseEntity<T> performRequestForSingleObj(String url, HttpMethod httpMethod,
                                                     MultiValueMap<String, String> queryParams,
                                                     Object requestBody,
                                                     ParameterizedTypeReference<T> responseType);

    <T> ResponseEntity<T> performRequestDynamic(String url, HttpMethod httpMethod,
                                                Map<String, String> queryParams,
                                                Object requestBody,
                                                ParameterizedTypeReference<T> responseType,
                                                HttpHeaders headers);
}
