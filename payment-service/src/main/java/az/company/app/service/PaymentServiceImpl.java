package az.company.app.service;

import az.company.app.config.CustomAuthorization;
import az.company.app.entity.RefundDetails;
import az.company.app.entity.Transaction;
import az.company.app.entity.TransactionType;
import az.company.app.errors.ErrorsFinal;
import az.company.app.errors.SuccessMessage;
import az.company.app.exception.ApplicationException;
import az.company.app.model.PurchaseAmountDto;
import az.company.app.model.RefundAmountDto;
import az.company.app.model.TopUpAmountDto;
import az.company.app.model.TransactionTypeEnum;
import az.company.app.repository.RefundDetailsRepository;
import az.company.app.repository.TransactionRepository;
import az.company.app.response.MessageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final CustomAuthorization customAuthorization;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RefundDetailsRepository refundDetailsRepository;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Top up balance.
     * @param topUpAmount An amount that want to top up balance.
     * @return A ResponseEntity object containing the success message and the operation details.
     */
    @Override
    public ResponseEntity<?> topUp(Long gsmNumber, TopUpAmountDto topUpAmountDto) {
        //to-do check gsmNumber exists
        Transaction transaction = new Transaction();
        transaction.setGsmNumber(gsmNumber);
        transaction.setAmount(topUpAmountDto.getTopUpAmount());
        TransactionType topUpTransaction = new TransactionType(TransactionTypeEnum.TOP_UP.getId());
        transaction.setTransactionType(topUpTransaction);
        Long createdBy = customAuthorization.getUserIdFromToken();
        transaction.setCreatedBy(createdBy);
        String transactionUUID = UUID.randomUUID().toString();
        transaction.setTransactionUUId(transactionUUID);
        transactionRepository.save(transaction);
        //to-do check update gsmNumber balance with customer API
        return MessageResponse.response(SuccessMessage.SUCCESS_ADD, null, null, HttpStatus.OK);
    }

    /**
     * Purchase something.
     * @param purchaseAmount An amount that want to purchase something.
     * @return A ResponseEntity object containing the success message and the operation details.
     */
    @Override
    public ResponseEntity<?> purchase(Long gsmNumber, PurchaseAmountDto purchaseAmountDto) {
        //to-do check gsmNumber exists
        Transaction transaction = new Transaction();
        transaction.setGsmNumber(gsmNumber);
        transaction.setAmount(purchaseAmountDto.getPurchaseAmount());
        TransactionType topUpTransaction = new TransactionType(TransactionTypeEnum.PURCHASE.getId());
        transaction.setTransactionType(topUpTransaction);
        Long createdBy = customAuthorization.getUserIdFromToken();
        transaction.setCreatedBy(createdBy);
        String transactionUUID = UUID.randomUUID().toString();
        transaction.setTransactionUUId(transactionUUID);
        transactionRepository.save(transaction);
        //to-do check update gsmNumber balance with customer API
        return MessageResponse.response(SuccessMessage.SUCCESS_ADD, null, null, HttpStatus.OK);
    }

    /**
     * Refund money by transactionId.
     * @param refundAmount An amount that want to refund from transaction.
     * @return A ResponseEntity object containing the success message and the operation details.
     */
    @Override
    public ResponseEntity<?> refund(String transactionId, RefundAmountDto refundAmountDto) {
        List<Transaction> transactions = transactionRepository.getByTransactionId(transactionId,TransactionTypeEnum.PURCHASE.getId());
        if (!transactions.isEmpty()){
            throw new ApplicationException(ErrorsFinal.TRANSACTION_NOT_FOUND,
            Map.ofEntries(Map.entry("transactionId",transactionId)));
        }
        if (transactions.size()>1) {
            throw new ApplicationException(ErrorsFinal.MULTIPLE_TRANSACTION_FOUND,
            Map.ofEntries(Map.entry("transactionId",transactionId)));
        }
        Transaction purchaseTransaction = transactions.get(0);
        Transaction refundTransaction = new Transaction();
        refundTransaction.setGsmNumber(purchaseTransaction.getGsmNumber());
        refundTransaction.setAmount(refundAmountDto.getRefundAmount());
        TransactionType topUpTransaction = new TransactionType(TransactionTypeEnum.REFUND.getId());
        refundTransaction.setTransactionType(topUpTransaction);
        Long createdBy = customAuthorization.getUserIdFromToken();
        refundTransaction.setCreatedBy(createdBy);
        String transactionUUID = UUID.randomUUID().toString();
        refundTransaction.setTransactionUUId(transactionUUID);
        transactionRepository.save(refundTransaction);
        RefundDetails refundDetails=new RefundDetails();
        refundDetails.setCreatedBy(createdBy);
        refundDetails.setPurchaseTransaction(purchaseTransaction);
        refundDetailsRepository.save(refundDetails);
        //to-do check update gsmNumber balance with customer API
        return MessageResponse.response(SuccessMessage.SUCCESS_ADD, null, null, HttpStatus.OK);
    }

}
