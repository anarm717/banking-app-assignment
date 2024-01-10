package az.company.app.controller;

import az.company.app.errors.SuccessMessage;
import az.company.app.logging.annotation.LogExecutionTime;
import az.company.app.model.AmountBaseDto;
import az.company.app.model.CustomerBaseDto;
import az.company.app.response.MessageResponse;
import az.company.app.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer")
@Tag(name = "Customer", description = "the Customer api")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @LogExecutionTime
    @Operation(summary = "get all customer", description = "there you can get all customer", tags = {"Customer"})
    @GetMapping
    @PreAuthorize("@customAuthorization.isValid('CustomerGetAll')")
    ResponseEntity<?> getAll(){
        return service.getActives();
    }

    @LogExecutionTime
    @Operation(summary = "add Customer", description = "add Customer", tags = {"Customer"})
    @PostMapping
    @PreAuthorize("@customAuthorization.isValid('CustomerAdd')")
    public ResponseEntity<?> add(@RequestBody @Valid CustomerBaseDto dto){
        return MessageResponse.response(SuccessMessage.SUCCESS_ADD, service.addCustomer(dto), null, HttpStatus.OK);
    }


    @LogExecutionTime
    @Operation(summary = "get customer", description = "there you can get customer by ID", tags = {"Customer"})
    @GetMapping("/{id}")
    @PreAuthorize("@customAuthorization.isValid('CustomerView')")
    public ResponseEntity<?> getById(@NotNull @PathVariable("id") Long id){
        return service.getById(id);
    }

    @LogExecutionTime
    @Operation(summary = "get customer by GsmNumber", description = "there you can get customer by Gsm Number", tags = {"Customer"})
    @GetMapping("/gsm-number/{gsmNumber}")
    @PreAuthorize("@customAuthorization.isValid('CustomerView')")
    public ResponseEntity<?> getByGsmNumber(@NotNull @PathVariable("gsmNumber") Long gsmNumber){
        return service.getByGsmNumber(gsmNumber);
    }

    @LogExecutionTime
    @Operation(summary = "update customer", description = "there you can update customer", tags = {"Customer"})
    @PutMapping("/{id}")
    @PreAuthorize("@customAuthorization.isValid('CustomerEdit')")
    ResponseEntity<?> updateCustomer (@RequestBody CustomerBaseDto customerBaseDto, @PathVariable Long id){
        return service.updateCustomer(customerBaseDto, id);
    }

    @LogExecutionTime
    @Operation(summary = "add amount to balance", description = "there you can add amount to balance", tags = {"Customer"})
    @PostMapping("/add-balance/{gsmNumber}")
    @PreAuthorize("@customAuthorization.isValid('CustomerEdit')")
    ResponseEntity<?> addBalance (@RequestBody AmountBaseDto amountBaseDto, @PathVariable Long gsmNumber){
        return service.addBalance(amountBaseDto, gsmNumber);
    }

    @LogExecutionTime
    @Operation(summary = "subtract amount from balance", description = "there you can subtract amount from balance", tags = {"Customer"})
    @PostMapping("/subtract-balance/{gsmNumber}")
    @PreAuthorize("@customAuthorization.isValid('CustomerEdit')")
    ResponseEntity<?> subtractBalance (@RequestBody AmountBaseDto amountBaseDto, @PathVariable Long gsmNumber){
        return service.subtractBalance(amountBaseDto, gsmNumber);
    }

    @LogExecutionTime
    @Operation(summary = "delete customer", description = "there you can delete customer", tags = {"Customer"})
    @DeleteMapping("/{id}")
    @PreAuthorize("@customAuthorization.isValid('CustomerDelete')")
    ResponseEntity<?> deleteCustomer(@PathVariable Long id){
        return service.deleteCustomer(id);
    }
}
