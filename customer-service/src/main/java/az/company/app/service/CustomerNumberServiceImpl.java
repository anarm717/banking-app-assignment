package az.company.app.service;

import az.company.app.config.CustomAuthorization;
import az.company.app.entity.Customer;
import az.company.app.entity.CustomerNumber;
import az.company.app.errors.ErrorsFinal;
import az.company.app.errors.SuccessMessage;
import az.company.app.exception.ApplicationException;
import az.company.app.mapper.CustomerNumberMapper;
import az.company.app.model.CustomerNumberBaseDto;
import az.company.app.model.CustomerNumberDto;
import az.company.app.repository.CustomerNumberRepository;
import az.company.app.repository.CustomerRepository;
import az.company.app.response.MessageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CustomerNumberServiceImpl implements CustomerNumberService {

    private final CustomAuthorization customAuthorization;

    private final PaymentApiService paymentApiService;

    @Autowired
    private CustomerNumberRepository customerNumberRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerNumberRepository customerNumberNumberRepository;

    @Autowired
    private CustomerNumberMapper customerNumberMapper;

    @PersistenceContext
    EntityManager entityManager;
    
    /**
     * Retrieves a customerNumber with the specified id from the repository and converts it to a CustomerNumberBaseDto object.
     * Throws an ApplicationException if a customerNumber with the specified id is not found.
     * @param id the id of the customerNumber to retrieve.
     * @return a ResponseEntity object containing the customerNumberBaseDto object, a null error message, and an OK status code.
     */
    @Override
    public ResponseEntity<?> getById(Long id) {
        CustomerNumber customerNumber = customerNumberRepository.findById(id).orElseThrow(() ->
                new ApplicationException(ErrorsFinal.DATA_NOT_FOUND, Map.ofEntries(Map.entry("id", id), Map.entry("name", "CustomerNumber"))));
        CustomerNumberDto dto = customerNumberMapper.entityToNumberDto(customerNumber);
        return MessageResponse.response(SuccessMessage.SUCCESS_GET, dto, null, HttpStatus.OK);
    }

    /**
     * Retrieves all active customerNumbers from the repository and converts them to CustomerNumberBaseDto objects.
     * @return a ResponseEntity object containing a list of CustomerNumberBaseDto objects, a null error message, and an OK status code.
     */
    @Override
    public ResponseEntity<?> getActives(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        List<CustomerNumber> find = customerNumberRepository.findAllByStatus('1',paging);
        List<CustomerNumberDto> dtos = customerNumberMapper.entityToNumberDtos(find);
        List<Integer> recordCount = customerNumberRepository.getAllCountByStatus('1');
        Map<String, Object> data = new HashMap<>();
        data.put("customerNumbers",dtos);
        data.put("recordCount",recordCount.get(0));
        return MessageResponse.response(SuccessMessage.SUCCESS_GET, data, null, HttpStatus.OK);
    }

    /**
     * Creates and saves a new customerNumber.
     * @param customerNumberBaseDto A CustomerNumberBaseDto object that contains the details of the customerNumber to be created.
     * @return A ResponseEntity object containing the success message and the created customerNumber details.
     */
    @Override
    public ResponseEntity<?> addCustomerNumber(CustomerNumberBaseDto customerNumberBaseDto) {
        Integer length = String.valueOf(customerNumberBaseDto.getGsmNumber()).length();
        if (!length.equals(9)) {
            throw new ApplicationException(ErrorsFinal.WRONG_GSM_NUMBER_LENGTH);
        }
        BigDecimal minBalanceLimit = new BigDecimal(100);
        if (customerNumberBaseDto.getBalance().compareTo(minBalanceLimit)<0) {
            throw new ApplicationException(ErrorsFinal.WRONG_INITIAL_BALANCE);
        }
        List<CustomerNumber> customerNumbers=customerNumberRepository.getByNumber(customerNumberBaseDto.getGsmNumber());
        if (!customerNumbers.isEmpty()) {
            throw new ApplicationException(ErrorsFinal.GSM_NUMBER_EXISTS,
            Map.ofEntries(Map.entry("gsmNumber", customerNumberBaseDto.getGsmNumber())));
        }
        Customer customer = customerRepository.findById(customerNumberBaseDto.getCustomerId()).orElseThrow(() ->
                new ApplicationException(ErrorsFinal.DATA_NOT_FOUND, Map.ofEntries(Map.entry("id", customerNumberBaseDto.getCustomerId()), Map.entry("name", "Customer"))));
        Long createdBy = customAuthorization.getUserIdFromToken();
        CustomerNumber customerNumber = customerNumberMapper.numberDtoToEntity(customerNumberBaseDto);
        customerNumber.setCreatedBy(createdBy);
        customerNumber.setCustomer(customer);
        customerNumberNumberRepository.save(customerNumber);
        CustomerNumberBaseDto posDto = customerNumberMapper.entityToNumberBaseDto(customerNumber);
        posDto.setBalance(customerNumberBaseDto.getBalance());
        try{
            paymentApiService.topUpBalance(customerNumber.getGsmNumber(), customerNumberBaseDto.getBalance());
        }
        catch (HttpClientErrorException.NotFound e){
            throw new ApplicationException(ErrorsFinal.DATA_NOT_FOUND);
        }
        catch (Exception e) {
            customerNumberRepository.delete(customerNumber);
            throw new ApplicationException(ErrorsFinal.CUSTOMER_SERVICE_ERROR);
        }
        return MessageResponse.response(SuccessMessage.SUCCESS_ADD, posDto, null, HttpStatus.OK);
    }

    /**
     * Deletes an existing customerNumber with the given ID.
     * @param id The ID of the customerNumber to be deleted.
     * @return A ResponseEntity object containing the success message and the deleted customerNumber details.
     * @throws ApplicationException if the customerNumber with the given ID is not found.
     */
    @Override
    public ResponseEntity<?> deleteCustomerNumber(Long id) {
        CustomerNumber customerNumber = customerNumberRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorsFinal.DATA_NOT_FOUND,
                        Map.ofEntries(Map.entry("id", id), Map.entry("name", "Vəzifəyə"))));
        customerNumberRepository.delete(customerNumber);
        CustomerNumberBaseDto customerNumberBaseDto = customerNumberMapper.entityToNumberBaseDto(customerNumber);
        return MessageResponse.response(SuccessMessage.SUCCESS_DELETE, customerNumberBaseDto, null, HttpStatus.OK);
    }

    @Override
    public CustomerNumberBaseDto getIdAndName(Long id) {
        Optional<CustomerNumber> customerNumber = customerNumberRepository.getByIdAndStatus(id, '1'); //.orElseThrow(()-> new ApplicationException(ErrorsFinal.DATA_NOT_FOUND));
        if(customerNumber.isPresent())
            return customerNumberMapper.entityToNumberBaseDto(customerNumber.get());
        return new CustomerNumberBaseDto();
    }

}
