package az.company.app.service;

import az.company.app.model.CustomerBaseDto;

import org.springframework.http.ResponseEntity;


public interface CustomerService {

    /**
     Retrieve a Customer entity by ID.
     @param id The ID of the entity to retrieve.
     @return A ResponseEntity containing the requested Customer entity, or an error response if the entity is not found.
     */
    ResponseEntity<?> getById(Long id);

    /**
     Retrieve all active Customer entities.
     @return A ResponseEntity containing a list of all active Customer entities, or an error response if the request fails.
     */
    ResponseEntity<?> getActives();

    /**
     Adds a new customer  to the system based on the provided data.
     @param dto the data transfer object containing the necessary parameters for creating the customer .
     @return true if the customer  was successfully added, false otherwise.
     */
    ResponseEntity<?> addCustomer(CustomerBaseDto dto);

    /**
     Update an existing Customer entity.
     @param customerBaseDto A CustomerBaseDto object containing the updated data for the entity.
     @param id The ID of the entity to update.
     @return A ResponseEntity containing the updated Customer entity, or an error response if the entity is not found or the update fails.
     */
    ResponseEntity<?> updateCustomer(CustomerBaseDto customerBaseDto, Long id);

    /**
     Delete a Customer entity by ID.
     @param id The ID of the entity to delete.
     @return A ResponseEntity indicating success or failure of the delete operation.
     */
    ResponseEntity<?> deleteCustomer(Long id);

    CustomerBaseDto getIdAndName(Long id);
}
