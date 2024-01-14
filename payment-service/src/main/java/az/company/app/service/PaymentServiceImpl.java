package az.company.app.service;

import az.company.app.config.CustomAuthorization;
import az.company.app.entity.RefundDetails;
import az.company.app.entity.Transaction;
import az.company.app.entity.TransactionType;
import az.company.app.errors.ErrorsFinal;
import az.company.app.errors.SuccessMessage;
import az.company.app.exception.ApplicationException;
import az.company.app.mapper.TransactionMapper;
import az.company.app.model.CustomerBaseDto;
import az.company.app.model.PurchaseAmountDto;
import az.company.app.model.RefundAmountDto;
import az.company.app.model.TopUpAmountDto;
import az.company.app.model.TransactionBaseDto;
import az.company.app.model.TransactionTypeEnum;
import az.company.app.repository.RefundDetailsRepository;
import az.company.app.repository.TransactionRepository;
import az.company.app.response.MessageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final CustomAuthorization customAuthorization;

    private final CustomerApiService customerApiService;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RefundDetailsRepository refundDetailsRepository;

    @PersistenceContext
    EntityManager entityManager;


    /**
     * Retrieves all products with a name that starts with, contains, or ends with the specified name and converts them to ProductBaseDto objects.
     * @param transactionUUID the name to search for.
     * @return a ResponseEntity object containing a deque of ProductBaseDto objects, a null error message, and an OK status code.
     */
    @Override
    public ResponseEntity<?> getByUUId(String transactionUUID){
        if (transactionUUID.length()>1) {
            List<Transaction> transactions = transactionRepository.getByTransactionId(transactionUUID);
            if(!transactions.isEmpty()) { 
                Transaction transaction = transactions.get(0);
                TransactionBaseDto transactionBaseDto = transactionMapper.entityToDto(transaction);
                return MessageResponse.response(SuccessMessage.SUCCESS_GET, transactionBaseDto, null, HttpStatus.OK);
            }
            return MessageResponse.response(SuccessMessage.SUCCESS_GET, null, null, HttpStatus.OK);
        }
        return MessageResponse.response(SuccessMessage.SUCCESS_GET, null, null, HttpStatus.OK);
    }

    /**
     * Retrieves all products with a name that starts with, contains, or ends with the specified name and converts them to ProductBaseDto objects.
     * @param transactionUUID the name to search for.
     * @return a ResponseEntity object containing a deque of ProductBaseDto objects, a null error message, and an OK status code.
     */
    @Override
    public ResponseEntity<?> getByGsmNumber(Long gsmNumber,int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        List<Transaction> transactions = transactionRepository.getByGsmNumber(gsmNumber,paging);
        if(!transactions.isEmpty()) { 
            List<TransactionBaseDto> transactionBaseDtos = transactionMapper.entityToDtos(transactions);
            List<Integer> recordCount = transactionRepository.getTransactionsCountByGsmNumber(gsmNumber);
            Map<String, Object> data = new HashMap<>();
            data.put("transactions",transactionBaseDtos);
            data.put("recordCount",recordCount.get(0));
            return MessageResponse.response(SuccessMessage.SUCCESS_GET, data, null, HttpStatus.OK);
        }
        return MessageResponse.response(SuccessMessage.SUCCESS_GET, null, null, HttpStatus.OK);
    }

    /**
     * Top up balance.
     * @param topUpAmount An amount that want to top up balance.
     * @return A ResponseEntity object containing the success message and the operation details.
     */
    @Override
    public ResponseEntity<?> topUp(Long gsmNumber, TopUpAmountDto topUpAmountDto) {
        CustomerBaseDto customer=null;
        try {
            customer = customerApiService.getCustomerByGsmNumber(gsmNumber);
        }
        catch (HttpClientErrorException.NotFound e){
            throw new ApplicationException(ErrorsFinal.GSM_NUMBER_NOT_FOUND,
            Map.ofEntries(Map.entry("gsmNumber",gsmNumber)));
        }
        catch (Exception e) {
            throw new ApplicationException(ErrorsFinal.CUSTOMER_SERVICE_ERROR);
        }
        if (customer == null) {
            throw new ApplicationException(ErrorsFinal.GSM_NUMBER_NOT_FOUND,
            Map.ofEntries(Map.entry("gsmNumber",gsmNumber)));
        }
        if (topUpAmountDto.getTopUpAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApplicationException(ErrorsFinal.WRONG_AMOUNT);
        }
        Transaction transaction = new Transaction();
        transaction.setGsmNumber(gsmNumber);
        transaction.setAmount(topUpAmountDto.getTopUpAmount());
        TransactionType topUpTransaction = new TransactionType(TransactionTypeEnum.TOP_UP.getId());
        topUpTransaction.setName(TransactionTypeEnum.TOP_UP.getName());
        transaction.setTransactionType(topUpTransaction);
        Long createdBy = customAuthorization.getUserIdFromToken();
        transaction.setCreatedBy(createdBy);
        String transactionUUID = UUID.randomUUID().toString();
        transaction.setTransactionUUId(transactionUUID);
        transactionRepository.save(transaction);
        try{
            customerApiService.addBalanceByGsmNumber(gsmNumber, topUpAmountDto.getTopUpAmount());
        }
        catch (HttpClientErrorException.NotFound e){
            throw new ApplicationException(ErrorsFinal.GSM_NUMBER_NOT_FOUND,
            Map.ofEntries(Map.entry("gsmNumber",gsmNumber)));
        }
        catch (Exception e) {
            transactionRepository.delete(transaction);
            throw new ApplicationException(ErrorsFinal.CUSTOMER_SERVICE_ERROR);
        }
        TransactionBaseDto transactionBaseDto = transactionMapper.entityToDto(transaction);
        return MessageResponse.response(SuccessMessage.SUCCESS_ADD, transactionBaseDto, null, HttpStatus.OK);
    }

    /**
     * Purchase something.
     * @param purchaseAmount An amount that want to purchase something.
     * @return A ResponseEntity object containing the success message and the operation details.
     */
    @Override
    public ResponseEntity<?> purchase(Long gsmNumber, PurchaseAmountDto purchaseAmountDto) {
        //to-do check gsmNumber exists
        if (purchaseAmountDto.getPurchaseAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApplicationException(ErrorsFinal.WRONG_AMOUNT);
        }
        Transaction transaction = new Transaction();
        transaction.setGsmNumber(gsmNumber);
        transaction.setAmount(purchaseAmountDto.getPurchaseAmount());
        TransactionType purchaseTransactionType = new TransactionType(TransactionTypeEnum.PURCHASE.getId());
        purchaseTransactionType.setName(TransactionTypeEnum.PURCHASE.getName());
        transaction.setTransactionType(purchaseTransactionType);
        Long createdBy = customAuthorization.getUserIdFromToken();
        transaction.setCreatedBy(createdBy);
        String transactionUUID = UUID.randomUUID().toString();
        transaction.setTransactionUUId(transactionUUID);
        transactionRepository.save(transaction);
        try{
            customerApiService.subtractBalanceByGsmNumber(gsmNumber, purchaseAmountDto.getPurchaseAmount());
        }
        catch (HttpClientErrorException.NotFound e){
            throw new ApplicationException(ErrorsFinal.GSM_NUMBER_NOT_FOUND,
            Map.ofEntries(Map.entry("gsmNumber",gsmNumber)));
        }
        catch (Exception e) {
            transactionRepository.delete(transaction);
            throw new ApplicationException(ErrorsFinal.CUSTOMER_SERVICE_ERROR);
        }
        TransactionBaseDto transactionBaseDto = transactionMapper.entityToDto(transaction);
        return MessageResponse.response(SuccessMessage.SUCCESS_ADD, transactionBaseDto, null, HttpStatus.OK);
    }

    /**
     * Refund money by transactionId.
     * @param refundAmount An amount that want to refund from transaction.
     * @return A ResponseEntity object containing the success message and the operation details.
     */
    @Override
    public ResponseEntity<?> refund(String transactionId, RefundAmountDto refundAmountDto) {
        if (refundAmountDto.getRefundAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApplicationException(ErrorsFinal.WRONG_AMOUNT);
        }
        List<Transaction> transactions = transactionRepository.getByTransactionId(transactionId,TransactionTypeEnum.PURCHASE.getId());
        if (transactions.isEmpty()){
            throw new ApplicationException(ErrorsFinal.TRANSACTION_NOT_FOUND,
            Map.ofEntries(Map.entry("transactionId",transactionId)));
        }
        if (transactions.size()>1) {
            throw new ApplicationException(ErrorsFinal.MULTIPLE_TRANSACTION_FOUND,
            Map.ofEntries(Map.entry("transactionId",transactionId)));
        }
        Transaction purchaseTransaction = transactions.get(0);
        List<BigDecimal> sumPurchaseAmounts = transactionRepository.getSumAmountByTransactionId(transactionId,TransactionTypeEnum.REFUND.getId());
        BigDecimal sumPurchaseAmount = sumPurchaseAmounts.get(0);
        if(sumPurchaseAmount==null) {
            sumPurchaseAmount = BigDecimal.ZERO;
        }
        if (purchaseTransaction.getAmount().compareTo(sumPurchaseAmount.add(refundAmountDto.getRefundAmount()))<0){
            throw new ApplicationException(ErrorsFinal.WRONG_REFUND_AMOUNT);
        }
        Transaction refundTransaction = new Transaction();
        refundTransaction.setGsmNumber(purchaseTransaction.getGsmNumber());
        refundTransaction.setAmount(refundAmountDto.getRefundAmount());
        TransactionType refunTransactionType = new TransactionType(TransactionTypeEnum.REFUND.getId());
        refunTransactionType.setName(TransactionTypeEnum.REFUND.getName());
        refundTransaction.setTransactionType(refunTransactionType);
        Long createdBy = customAuthorization.getUserIdFromToken();
        refundTransaction.setCreatedBy(createdBy);
        String transactionUUID = UUID.randomUUID().toString();
        refundTransaction.setTransactionUUId(transactionUUID);
        transactionRepository.save(refundTransaction);
        RefundDetails refundDetails=new RefundDetails();
        refundDetails.setCreatedBy(createdBy);
        refundDetails.setPurchaseTransaction(purchaseTransaction);
        refundDetails.setTransaction(refundTransaction);
        refundDetailsRepository.save(refundDetails);
        try{
            customerApiService.addBalanceByGsmNumber(purchaseTransaction.getGsmNumber(), refundAmountDto.getRefundAmount());
        }
        catch (HttpClientErrorException.NotFound e){
            throw new ApplicationException(ErrorsFinal.DATA_NOT_FOUND);
        }
        catch (Exception e) {
            transactionRepository.delete(refundTransaction);
            refundDetailsRepository.delete(refundDetails);
            throw new ApplicationException(ErrorsFinal.CUSTOMER_SERVICE_ERROR);
        }
        TransactionBaseDto transactionBaseDto = transactionMapper.entityToDto(refundTransaction);
        return MessageResponse.response(SuccessMessage.SUCCESS_ADD, transactionBaseDto, null, HttpStatus.OK);
    }

}
