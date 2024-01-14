package az.company.app.service;

import az.company.app.model.AmountBaseDto;
import az.company.app.model.CustomerNumberBaseDto;

import org.springframework.http.ResponseEntity;


public interface CustomerNumberService {

    /**
     Retrieve a CustomerNumber entity by ID.
     @param id The ID of the entity to retrieve.
     @return A ResponseEntity containing the requested CustomerNumber entity, or an error response if the entity is not found.
     */
    ResponseEntity<?> getById(Long id);

    /**
     Retrieve all active CustomerNumber entities.
     @return A ResponseEntity containing a list of all active CustomerNumber entities, or an error response if the request fails.
     */
    ResponseEntity<?> getActives(int page, int size);

    /**
     Adds a new customerNumber  to the system based on the provided data.
     @param dto the data transfer object containing the necessary parameters for creating the customerNumber .
     @return true if the customerNumber  was successfully added, false otherwise.
     */
    ResponseEntity<?> addCustomerNumber(CustomerNumberBaseDto dto);

    /**
     Delete a CustomerNumber entity by ID.
     @param id The ID of the entity to delete.
     @return A ResponseEntity indicating success or failure of the delete operation.
     */
    ResponseEntity<?> deleteCustomerNumber(Long id);

    CustomerNumberBaseDto getIdAndName(Long id);
}
