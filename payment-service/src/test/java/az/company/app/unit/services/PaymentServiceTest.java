package az.company.app.unit.services;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import az.company.app.entity.Transaction;
import az.company.app.entity.TransactionType;
import az.company.app.mapper.TransactionMapper;
import az.company.app.model.TransactionBaseDto;
import az.company.app.repository.RefundDetailsRepository;
import az.company.app.repository.TransactionRepository;
import az.company.app.response.ResponseModelDTO;
import az.company.app.service.PaymentServiceImpl;
import az.company.app.unit.base.BaseServiceTest;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest extends BaseServiceTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private RefundDetailsRepository refundDetailsRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private Pageable pageableMock;
    
    @Test
    public void givenTransactionID_whenTransactionID_thenTransactionDetails() {

        // Given

        String transactionUUID = "Test-transaction-UUID";

        Transaction transaction = new Transaction(Long.valueOf(1));
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setGsmNumber(Long.valueOf(505555555));
        transaction.setStatus('1');
        transaction.setTransactionType(new TransactionType(Long.valueOf(1)));
        transaction.setTransactionUUId(transactionUUID);

        TransactionBaseDto transactionBaseDto = TransactionBaseDto.builder()
                .amount(BigDecimal.valueOf(100))
                .gsmNumber(Long.valueOf(505555555))
                .transactionType("Top-up")
                .transactionUUId("test-transaction-id")
                .build();

        List<Transaction> transactions = List.of(transaction);


        // When
        when(transactionRepository.getByTransactionId(transactionUUID)).thenReturn(transactions);
        when(transactionMapper.entityToDto(transaction)).thenReturn(transactionBaseDto);

        // then
        ResponseEntity<?> response = paymentService.getByUUId(transactionUUID);

        ResponseModelDTO result = (ResponseModelDTO) response.getBody();

        TransactionBaseDto transactionBaseDto2 = (TransactionBaseDto) result.getData();

        assertEquals(transactionBaseDto2.getAmount(), transactionBaseDto.getAmount());
        assertEquals(transactionBaseDto2.getGsmNumber(), transactionBaseDto.getGsmNumber());
        assertEquals(transactionBaseDto2.getTransactionType(), transactionBaseDto.getTransactionType());

        verify(transactionRepository,times(1)).getByTransactionId(any(String.class));
        verify(transactionMapper, times(1)).entityToDto(any(Transaction.class));

    }

    @Test
    public void givenGsmNumber_whenGsmNumber_thenTransactionDetails() {

        // Given

        Long gsmNumber = Long.valueOf(505555555);

        Transaction transaction = new Transaction(Long.valueOf(1));
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setGsmNumber(Long.valueOf(505555555));
        transaction.setStatus('1');
        transaction.setTransactionType(new TransactionType(Long.valueOf(1)));
        transaction.setTransactionUUId("test-transaction-Id");

        TransactionBaseDto transactionBaseDto = TransactionBaseDto.builder()
                .amount(BigDecimal.valueOf(100))
                .gsmNumber(Long.valueOf(505555555))
                .transactionType("Top-up")
                .transactionUUId("test-transaction-id")
                .build();

        List<Transaction> transactions = List.of(transaction);
        Pageable pageable= PageRequest.of(0, 10);

        // When
        when(transactionRepository.getByGsmNumber(gsmNumber,pageable)).thenReturn(transactions);
        when(transactionRepository.getTransactionsCountByGsmNumber(gsmNumber)).thenReturn(List.of(1));
        when(transactionMapper.entityToDtos(transactions)).thenReturn(List.of(transactionBaseDto));

        // then
        ResponseEntity<?> response = paymentService.getByGsmNumber(gsmNumber,0,10);

        ResponseModelDTO result = (ResponseModelDTO) response.getBody();

        HashMap data = (HashMap) result.getData();
        
        List<TransactionBaseDto> transactionBaseDto2 = (List<TransactionBaseDto>) data.get("transactions");
        Integer recordcount = (Integer) data.get("recordCount");
        assertEquals(recordcount, 1);
        assertEquals(transactionBaseDto2.get(0).getAmount(), transactionBaseDto.getAmount());
        assertEquals(transactionBaseDto2.get(0).getGsmNumber(), transactionBaseDto.getGsmNumber());
        assertEquals(transactionBaseDto2.get(0).getTransactionType(), transactionBaseDto.getTransactionType());

        verify(transactionRepository,times(1)).getByGsmNumber(any(Long.class), any(Pageable.class));
        verify(transactionRepository, times(1)).getTransactionsCountByGsmNumber(any(Long.class));
        verify(transactionMapper, times(1)).entityToDtos(any(List.class));
    }
}
    


