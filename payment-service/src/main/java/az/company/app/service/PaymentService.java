package az.company.app.service;

import org.springframework.http.ResponseEntity;

import az.company.app.model.PurchaseAmountDto;
import az.company.app.model.RefundAmountDto;
import az.company.app.model.TopUpAmountDto;


public interface PaymentService {

    /**
     Top up balance by gsmNumber.
     @param dto the data transfer object containing the necessary parameters for topUp balance .
     @return true if the operation is successfully finished, false otherwise.
     */
    ResponseEntity<?> topUp(Long gsmNumber, TopUpAmountDto topUpAmountDto);

    /**
     Purchase something by gsmNumber.
     @param dto the data transfer object containing the necessary parameters for purchase .
     @return true if the operation is successfully finished, false otherwise.
     */
    ResponseEntity<?> purchase(Long gsmNumber, PurchaseAmountDto purchaseAmountDto);

    /**
     Refund amount by transactionId.
     @param dto the data transfer object containing the necessary parameters for purchase .
     @return true if the operation is successfully finished, false otherwise.
     */
    ResponseEntity<?> refund(String transactionId, RefundAmountDto refundAmountDto);

}
