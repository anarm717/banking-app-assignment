package az.company.app.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import az.company.app.model.AmountDto;
import az.company.app.model.ApiResponse;
import az.company.app.model.CustomerBaseDto;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpMethod;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class CustomerApiServiceImpl implements CustomerApiService {

    @Value("${customer_service.url}")
    private String customerApiUrl;

    private final ApiCallService apiCallService;

    public CustomerBaseDto getCustomerByGsmNumber(Long gsmNumber) {
        String urlToApiService = customerApiUrl + "/gsm-number/"+gsmNumber;
        ResponseEntity<ApiResponse<CustomerBaseDto>> response = apiCallService.performRequestForSingleObj(urlToApiService, HttpMethod.GET, null, null,
                new ParameterizedTypeReference<>() {
                });
        ApiResponse<CustomerBaseDto> result = response.getBody();
        if (result != null) {
            return result.getData();
        }
        return null;
    }

    public CustomerBaseDto addBalanceByGsmNumber(Long gsmNumber, BigDecimal amount) {
        String urlToApiService = customerApiUrl + "/add-balance/"+gsmNumber;
        AmountDto amountDto = new AmountDto();
        amountDto.setAmount(amount);
        ResponseEntity<ApiResponse<CustomerBaseDto>> response = apiCallService.performRequestForSingleObj(urlToApiService, HttpMethod.POST, null, amountDto,
                new ParameterizedTypeReference<>() {
                });
        ApiResponse<CustomerBaseDto> result = response.getBody();
        if (result != null) {
            return result.getData();
        }
        return null;
    }

    public CustomerBaseDto subtractBalanceByGsmNumber(Long gsmNumber, BigDecimal amount) {
        String urlToApiService = customerApiUrl + "/subtract-balance/"+gsmNumber;
        AmountDto amountDto = new AmountDto();
        amountDto.setAmount(amount);
        ResponseEntity<ApiResponse<CustomerBaseDto>> response = apiCallService.performRequestForSingleObj(urlToApiService, HttpMethod.POST, null, amountDto,
                new ParameterizedTypeReference<>() {
                });
        ApiResponse<CustomerBaseDto> result = response.getBody();
        if (result != null) {
            return result.getData();
        }
        return null;
    }
}
