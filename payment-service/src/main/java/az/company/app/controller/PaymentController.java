package az.company.app.controller;

import az.company.app.logging.annotation.LogExecutionTime;
import az.company.app.model.PurchaseAmountDto;
import az.company.app.model.RefundAmountDto;
import az.company.app.model.TopUpAmountDto;
import az.company.app.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
@Tag(name = "Payment", description = "the Payment api")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @LogExecutionTime
    @Operation(summary = "get transaction by id", description = "there you can get transactions by ID", tags = {"Payment"})
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<?> getByUUId(@NotNull @PathVariable("transactionId") String transactionId){
        return service.getByUUId(transactionId);
    }

    @LogExecutionTime
    @Operation(summary = "get transactions by gsmNumber", description = "there you can get transactions by gsmNumber", tags = {"Payment"})
    @GetMapping("/transactions/{gsmNumber}")
    public ResponseEntity<?> getByGsmNumber(@NotNull @PathVariable("gsmNumber") Long gsmNumber,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size){
        return service.getByGsmNumber(gsmNumber,page,size);
    }

    @LogExecutionTime
    @Operation(summary = "top-up balance", description = "there you top-up balance by customerId", tags = {"Payment"})
    @PostMapping("/top-up/{gsmNumber}")
    @PreAuthorize("@customAuthorization.isValid('TopUpBalance')")
    ResponseEntity<?> topUp(@PathVariable("gsmNumber") Long gsmNumber, @RequestBody TopUpAmountDto topUpAmount){
        return service.topUp(gsmNumber, topUpAmount);
    }

    @LogExecutionTime
    @Operation(summary = "purchase something", description = "there you purchase something by customerId", tags = {"Payment"})
    @PostMapping("/purchase/{gsmNumber}")
    @PreAuthorize("@customAuthorization.isValid('PurchaseRole')")
    ResponseEntity<?> purchase(@PathVariable("gsmNumber") Long gsmNumber, @RequestBody PurchaseAmountDto purchaseAmount){
        return service.purchase(gsmNumber, purchaseAmount);
    }

    @LogExecutionTime
    @Operation(summary = "refund amount", description = "there you refund amount from transaction", tags = {"Payment"})
    @PostMapping("/refund/{transactionId}")
    @PreAuthorize("@customAuthorization.isValid('RefundRole')")
    ResponseEntity<?> refund(@PathVariable("transactionId") String transactionId, @RequestBody RefundAmountDto refundAmountDto){
        return service.refund(transactionId, refundAmountDto);
    }

}
