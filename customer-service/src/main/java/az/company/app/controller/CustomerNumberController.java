package az.company.app.controller;

import az.company.app.errors.SuccessMessage;
import az.company.app.logging.annotation.LogExecutionTime;
import az.company.app.model.CustomerNumberBaseDto;
import az.company.app.response.MessageResponse;
import az.company.app.service.CustomerNumberService;
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
@RequestMapping("/api/v1/customer-number")
@Tag(name = "Customer", description = "the Customer api")
@SecurityRequirement(name = "bearerAuth")
public class CustomerNumberController {

    @Autowired
    private CustomerNumberService service;

    @LogExecutionTime
    @Operation(summary = "get all customer numbers", description = "there you can get all customer numbers", tags = {"CustomerNumber"})
    @GetMapping
    @PreAuthorize("@customAuthorization.isValid('CustomerGetAll')")
    ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size){
        return service.getActives(page,size);
    }

    @LogExecutionTime
    @Operation(summary = "add Number to Customer", description = "add Number to Customer", tags = {"CustomerNumber"})
    @PostMapping
    @PreAuthorize("@customAuthorization.isValid('CustomerAdd')")
    public ResponseEntity<?> add(@RequestBody @Valid CustomerNumberBaseDto dto){
        return MessageResponse.response(SuccessMessage.SUCCESS_ADD, service.addCustomerNumber(dto), null, HttpStatus.OK);
    }


    @LogExecutionTime
    @Operation(summary = "get customer number", description = "there you can get customer number by ID", tags = {"CustomerNumber"})
    @GetMapping("/{id}")
    @PreAuthorize("@customAuthorization.isValid('CustomerView')")
    public ResponseEntity<?> getById(@NotNull @PathVariable("id") Long id){
        return service.getById(id);
    }

    @LogExecutionTime
    @Operation(summary = "delete customer number", description = "there you can delete customer number", tags = {"CustomerNumber"})
    @DeleteMapping("/{id}")
    @PreAuthorize("@customAuthorization.isValid('CustomerDelete')")
    ResponseEntity<?> deleteCustomerNumber(@PathVariable Long id){
        return service.deleteCustomerNumber(id);
    }
}
