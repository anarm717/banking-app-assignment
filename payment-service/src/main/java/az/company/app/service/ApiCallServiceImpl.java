package az.company.app.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiCallServiceImpl implements ApiCallService {

    private final RestTemplate restTemplate;

    private final HttpServletRequest request;

    @Override
    public <T> ResponseEntity<T> performRequestForSingleObj(String url, HttpMethod httpMethod, MultiValueMap<String,
            String> queryParams, Object requestBody, ParameterizedTypeReference<T> responseType) {
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<T> responseEntity;
        if (queryParams != null) {
            responseEntity = restTemplate.exchange(
                    url,
                    httpMethod,
                    requestEntity,
                    responseType,
                    queryParams);
        } else {
            responseEntity = restTemplate.exchange(
                    url,
                    httpMethod,
                    requestEntity,
                    responseType);
        }

        return responseEntity;
    }

    @Override
    public <T> ResponseEntity<T> performRequestDynamic(String url,
                                                       HttpMethod httpMethod,
                                                       Map<String, String> queryParams,
                                                       Object requestBody,
                                                       ParameterizedTypeReference<T> responseType,
                                                       HttpHeaders headers)
    {
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<T> responseEntity;
        if (queryParams != null) {
            responseEntity = restTemplate.exchange(
                    url,
                    httpMethod,
                    requestEntity,
                    responseType,
                    queryParams);
        } else {
            responseEntity = restTemplate.exchange(
                    url,
                    httpMethod,
                    requestEntity,
                    responseType);
        }

        return responseEntity;
    }
}
