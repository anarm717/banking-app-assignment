package az.company.app.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import az.company.app.model.ApiResponse;
import az.company.app.model.CustomerBaseDto;
import az.company.app.model.TopUpAmountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class PaymentApiServiceImpl implements PaymentApiService {

    @Value("${payment_service.url}")
    private String paymentApiUrl;

    private final ApiCallService apiCallService;

    public CustomerBaseDto topUpBalance(Long gsmNumber, BigDecimal amount) {
        String urlToApiService = paymentApiUrl + "/top-up/"+gsmNumber;
        TopUpAmountDto amountDto = new TopUpAmountDto();
        amountDto.setTopUpAmount(amount);
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
