package az.company.app.unit.controller;

import az.company.app.config.CustomAuthorization;
import az.company.app.errors.SuccessMessage;
import az.company.app.mapper.TransactionMapper;
import az.company.app.model.CustomerBaseDto;
import az.company.app.model.PurchaseAmountDto;
import az.company.app.model.RefundAmountDto;
import az.company.app.model.TopUpAmountDto;
import az.company.app.model.TransactionBaseDto;
import az.company.app.response.MessageResponse;
import az.company.app.service.AuthService;
import az.company.app.service.CustomerApiService;
import az.company.app.service.PaymentService;
import az.company.app.unit.base.BaseControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;

@TestPropertySource(locations = "classpath:test.properties")
class PaymentControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private TransactionMapper transactionMapper;

    @MockBean
    private CustomAuthorization customAuthorization;

    @Value("${testing.mock.token}")
    private String token;

    HttpHeaders headers = new HttpHeaders();

    @MockBean
    private CustomerApiService customerApiService;

    @Test
    public void givenTransactionIdRequest_whenGetByUUID_ReturnTransaction() throws Exception {

        // Given
        TransactionBaseDto transactionBaseDto = TransactionBaseDto.builder()
                .amount(BigDecimal.valueOf(100))
                .gsmNumber(Long.valueOf(505555555))
                .transactionType("Top-up")
                .transactionUUId("test-transaction-id")
                .build();
        
        String transactionUUID = "test-transaction-id";
        ResponseEntity response = MessageResponse.response(SuccessMessage.SUCCESS_GET, transactionBaseDto, null, HttpStatus.OK);
        // when
        when(paymentService.getByUUId(transactionUUID)).thenReturn(response);
        AuthService authService = mock(AuthService.class);
        when(authService.validateToken(token)).thenReturn(true);
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/payment/transaction/test-transaction-id").headers(headers))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount").value(BigDecimal.valueOf(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.gsmNumber").value(Long.valueOf(505555555)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.transactionType").value("Top-up"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.transactionUUId").value("test-transaction-id"));
        }
        
    @Test
    public void givenGsmNumberRequest_whenGetByGsmNumber_ReturnTransactions() throws Exception {

        List<TransactionBaseDto> transactionBaseDto = List.of(TransactionBaseDto.builder()
                .amount(BigDecimal.valueOf(100))
                .gsmNumber(Long.valueOf(505555555))
                .transactionType("Top-up")
                .transactionUUId("test-transaction-id")
                .build());
        
        Long gsmNumber = Long.valueOf(505555555);
        ResponseEntity response = MessageResponse.response(SuccessMessage.SUCCESS_GET, transactionBaseDto, null, HttpStatus.OK);
        // when
        when(paymentService.getByGsmNumber(gsmNumber,0,10)).thenReturn(response);

        AuthService authService = mock(AuthService.class);
        when(authService.validateToken(token)).thenReturn(true);
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/payment/transactions/505555555").headers(headers))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].amount").value(BigDecimal.valueOf(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].gsmNumber").value(Long.valueOf(505555555)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].transactionType").value("Top-up"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].transactionUUId").value("test-transaction-id"));

    }

    @Test
    public void givenTopUpAmountRequest_whenTopUpAmount_ReturnTransaction() throws Exception {

        //Given
        TopUpAmountDto request = TopUpAmountDto.builder()
                .topUpAmount(BigDecimal.valueOf(100))
                .build();
        TransactionBaseDto transactionBaseDto = TransactionBaseDto.builder()
                .amount(BigDecimal.valueOf(100))
                .gsmNumber(Long.valueOf(505555555))
                .transactionType("Top-up")
                .transactionUUId("test-transaction-id")
                .build();
        
        Long gsmNumber = Long.valueOf(505555555);
        ResponseEntity response = MessageResponse.response(SuccessMessage.SUCCESS_ADD, transactionBaseDto, null, HttpStatus.OK);
        
        // when
        when(paymentService.topUp(gsmNumber,request)).thenReturn(response);
        CustomerBaseDto customerBaseDto = new CustomerBaseDto();
        when(customerApiService.getCustomerByGsmNumber(gsmNumber)).thenReturn(customerBaseDto);
        when(customAuthorization.isValid(any(String.class))).thenReturn(true);

        AuthService authService = mock(AuthService.class);
        when(authService.validateToken(token)).thenReturn(true);
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/payment/top-up/505555555").headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    public void givenPurchaseAmountRequest_whenPurchase_ReturnTransaction() throws Exception {

        //Given
        PurchaseAmountDto request = PurchaseAmountDto.builder()
                .purchaseAmount(BigDecimal.valueOf(50))
                .build();
        List<TransactionBaseDto> transactionBaseDto = List.of(TransactionBaseDto.builder()
                .amount(BigDecimal.valueOf(50))
                .gsmNumber(Long.valueOf(505555555))
                .transactionType("Purchase")
                .transactionUUId("test-transaction-id")
                .build());
        
        Long gsmNumber = Long.valueOf(505555555);
        ResponseEntity response = MessageResponse.response(SuccessMessage.SUCCESS_GET, transactionBaseDto, null, HttpStatus.OK);
        
        // when isValid
        when(paymentService.purchase(gsmNumber, request)).thenReturn(response);
        when(customAuthorization.isValid(any(String.class))).thenReturn(true);
        CustomerBaseDto customerBaseDto = new CustomerBaseDto();
        when(customerApiService.getCustomerByGsmNumber(gsmNumber)).thenReturn(customerBaseDto);

        AuthService authService = mock(AuthService.class);
        when(authService.validateToken(token)).thenReturn(true);
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/payment/purchase/505555555").headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    public void givenRefundAmountRequest_whenRefund_ReturnTransaction() throws Exception {

        //Given
        RefundAmountDto refundAmountDto = RefundAmountDto.builder()
                .refundAmount(BigDecimal.valueOf(30))
                .build();
        List<TransactionBaseDto> transactionBaseDto = List.of(TransactionBaseDto.builder()
                .amount(BigDecimal.valueOf(30))
                .gsmNumber(Long.valueOf(505555555))
                .transactionType("Refund")
                .transactionUUId("test-transaction-id")
                .build());
        
        String  transactionUUID = "test-transaction-id";
        ResponseEntity response = MessageResponse.response(SuccessMessage.SUCCESS_GET, transactionBaseDto, null, HttpStatus.OK);
        // when
        when(paymentService.refund(transactionUUID,refundAmountDto)).thenReturn(response);
        when(customAuthorization.isValid(any(String.class))).thenReturn(true);
        AuthService authService = mock(AuthService.class);
        when(authService.validateToken(token)).thenReturn(true);
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/payment/purchase/505555555").headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundAmountDto)))
                .andExpect(status().isOk());

    }
}