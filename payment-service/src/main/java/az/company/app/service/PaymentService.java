package az.company.app.service;

import org.springframework.http.ResponseEntity;

import az.company.app.model.PurchaseAmountDto;
import az.company.app.model.RefundAmountDto;
import az.company.app.model.TopUpAmountDto;


public interface PaymentService {

    /**
     Retrieve a Transaction entity by ID.
     @param transactionUUID The ID of the entity to retrieve.
     @return A ResponseEntity containing the requested Transaction entity, or an error response if the entity is not found.
     */
    ResponseEntity<?> getByUUId(String transactionUUID);

    /**
     Retrieve a Transaction entity by ID.
     @param gsmNumber The gsmNumber of the entity to retrieve.
     @return A ResponseEntity containing the requested Transaction entity, or an error response if the entity is not found.
     */
    ResponseEntity<?> getByGsmNumber(Long gsmNumber,int page, int size);

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
