package az.company.app.integration;

import az.company.app.model.ApiResponse;
import az.company.app.model.PurchaseAmountDto;
import az.company.app.model.RefundAmountDto;
import az.company.app.model.TopUpAmountDto;
import az.company.app.model.TransactionBaseDto;
import az.company.app.unit.base.BaseControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@TestMethodOrder(OrderAnnotation.class)
class PaymentControllerTest extends BaseControllerTest {

    @Value("${testing.mock.token}")
    private String token;

    HttpHeaders headers = new HttpHeaders();

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String URL = "/api/v1/payment/";
    
    private static String transactionId;

    private static String purchaseTransactionId;
   @Test
   @Order(1)
   public void givenGsmNumberRequest_whenGetByGsmNumber_ReturnTransactions() throws Exception {
        String gsmNumber = "505005050";
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        String transactionUrl = URL.concat("transactions/").concat(gsmNumber);
        ResponseEntity<ApiResponse<Object>> responseEntity = restTemplate.exchange(
                transactionUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                });

        ApiResponse<Object> result = responseEntity.getBody();
        LinkedHashMap responseData = (LinkedHashMap) result.getData();
        assertNotNull(responseData);
        Integer recordCount = (Integer) responseData.get("recordCount");
	assertTrue(recordCount>1);
   }
   @Test
   @Order(2)
   public void givenTopUpAmountRequest_whenTopUpAmount_ReturnTransaction() throws Exception {
        //Given
        TopUpAmountDto request = TopUpAmountDto.builder()
                .topUpAmount(BigDecimal.valueOf(100))
                .build();
        String gsmNumber = "505005050";
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TopUpAmountDto> entity = new HttpEntity<>(request, headers);
        String transactionUrl = URL.concat("top-up/").concat(gsmNumber);
        ResponseEntity<ApiResponse<TransactionBaseDto>> responseEntity = restTemplate.exchange(
                transactionUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
                });

        ApiResponse<TransactionBaseDto> result = responseEntity.getBody();
        TransactionBaseDto transaction = result.getData();
        transactionId = transaction.getTransactionUUId();
        assertNotNull(transaction);
	assertEquals(transaction.getAmount(),BigDecimal.valueOf(100));
   }
   @Test
   @Order(3)
   public void givenWrongTopUpAmountRequest_whenTopUpAmount_ReturnTransaction() throws Exception {
        //Given
        TopUpAmountDto request = TopUpAmountDto.builder()
                .topUpAmount(BigDecimal.valueOf(-1))
                .build();
        String gsmNumber = "505005050";
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TopUpAmountDto> entity = new HttpEntity<>(request, headers);
        String transactionUrl = URL.concat("top-up/").concat(gsmNumber);
        ResponseEntity<ApiResponse<TransactionBaseDto>> responseEntity = restTemplate.exchange(
                transactionUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
                });
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
   }
   @Test
   @Order(4)
    public void givenTransactionIdRequest_whenGetByUUID_ReturnTransaction() throws Exception {
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        String transactionUrl = URL.concat("transaction/").concat(transactionId);
        ResponseEntity<ApiResponse<TransactionBaseDto>> responseEntity = restTemplate.exchange(
                transactionUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                });
        ApiResponse<TransactionBaseDto> result = responseEntity.getBody();
        TransactionBaseDto transaction = result.getData();
        assertNotNull(transaction);
	assertNotNull(transaction.getAmount());
   }
   @Test
   @Order(5)
   public void givenPurchaseAmountRequest_whenPurchase_ReturnTransaction() throws Exception {
        //Given
        PurchaseAmountDto request = PurchaseAmountDto.builder()
                .purchaseAmount(BigDecimal.valueOf(50))
                .build();
        String gsmNumber = "505005050";
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PurchaseAmountDto> entity = new HttpEntity<>(request, headers);
        String transactionUrl = URL.concat("purchase/").concat(gsmNumber);
        ResponseEntity<ApiResponse<TransactionBaseDto>> responseEntity = restTemplate.exchange(
                transactionUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
                });

        ApiResponse<TransactionBaseDto> result = responseEntity.getBody();
        TransactionBaseDto transaction = result.getData();
        assertNotNull(transaction);
	assertEquals(transaction.getAmount(),BigDecimal.valueOf(50));
        purchaseTransactionId = transaction.getTransactionUUId();
   }
   @Test
   @Order(6)
   public void givenRefundAmountRequest_whenRefund_ReturnTransaction() throws Exception {
        //Given
        RefundAmountDto refundAmountDto = RefundAmountDto.builder()
                .refundAmount(BigDecimal.valueOf(30))
                .build();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RefundAmountDto> entity = new HttpEntity<>(refundAmountDto, headers);
        String transactionUrl = URL.concat("refund/").concat(purchaseTransactionId);
        ResponseEntity<ApiResponse<TransactionBaseDto>> responseEntity = restTemplate.exchange(
                transactionUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
                });

        ApiResponse<TransactionBaseDto> result = responseEntity.getBody();
        TransactionBaseDto transaction = result.getData();
        assertNotNull(transaction);
	assertEquals(transaction.getAmount(),BigDecimal.valueOf(30));
   }
   @Test
   @Order(7)
   public void givenRefundAmountHigherThanPurchaseAmountRequest_whenRefund_ReturnTransaction() throws Exception {
        //Given
        RefundAmountDto refundAmountDto = RefundAmountDto.builder()
                .refundAmount(BigDecimal.valueOf(300))
                .build();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RefundAmountDto> entity = new HttpEntity<>(refundAmountDto, headers);
        String transactionUrl = URL.concat("refund/").concat(purchaseTransactionId);
        ResponseEntity<ApiResponse<TransactionBaseDto>> responseEntity = restTemplate.exchange(
                transactionUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
                });
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
   }
   @Test
   @Order(8)
   public void givenTopUpAmountAndWrongGsmNumberRequest_whenTopUpAmount_ReturnTransaction() throws Exception {
        //Given
        TopUpAmountDto request = TopUpAmountDto.builder()
                .topUpAmount(BigDecimal.valueOf(100))
                .build();
        String gsmNumber = "501234567";
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TopUpAmountDto> entity = new HttpEntity<>(request, headers);
        String transactionUrl = URL.concat("top-up/").concat(gsmNumber);
        ResponseEntity<ApiResponse<TransactionBaseDto>> responseEntity = restTemplate.exchange(
                transactionUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
                });
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
   }
   @Test
   @Order(9)
   public void givenNegativePurchaseAmountRequest_whenPurchase_ReturnTransaction() throws Exception {
        //Given
        PurchaseAmountDto request = PurchaseAmountDto.builder()
                .purchaseAmount(BigDecimal.valueOf(-50))
                .build();
        String gsmNumber = "505005050";
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PurchaseAmountDto> entity = new HttpEntity<>(request, headers);
        String transactionUrl = URL.concat("purchase/").concat(gsmNumber);
        ResponseEntity<ApiResponse<TransactionBaseDto>> responseEntity = restTemplate.exchange(
                transactionUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
                });

        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
   }
}