package az.company.app.service;

import az.company.app.config.CustomAuthorization;
import az.company.app.entity.Customer;
import az.company.app.entity.CustomerNumber;
import az.company.app.errors.ErrorsFinal;
import az.company.app.errors.SuccessMessage;
import az.company.app.exception.ApplicationException;
import az.company.app.mapper.CustomerMapper;
import az.company.app.model.AmountBaseDto;
import az.company.app.model.CustomerBaseDto;
import az.company.app.repository.CustomerNumberRepository;
import az.company.app.repository.CustomerRepository;
import az.company.app.response.MessageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomAuthorization customAuthorization;

    private final PaymentApiService paymentApiService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerNumberRepository customerNumberRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @PersistenceContext
    EntityManager entityManager;
    
    /**
     * Retrieves a customer with the specified id from the repository and converts it to a CustomerBaseDto object.
     * Throws an ApplicationException if a customer with the specified id is not found.
     * @param id the id of the customer to retrieve.
     * @return a ResponseEntity object containing the customerBaseDto object, a null error message, and an OK status code.
     */
    @Override
    public ResponseEntity<?> getById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new ApplicationException(ErrorsFinal.DATA_NOT_FOUND, Map.ofEntries(Map.entry("id", id), Map.entry("name", "Customer"))));
        CustomerBaseDto dto = customerMapper.entityToDto(customer);
        return MessageResponse.response(SuccessMessage.SUCCESS_GET, dto, null, HttpStatus.OK);
    }

    /**
     * Retrieves a customer with the specified gsmNumber from the repository and converts it to a CustomerBaseDto object.
     * Throws an ApplicationException if a customer with the specified gsmNumber is not found.
     * @param gsmNumber the gsmNumber of the customer to retrieve.
     * @return a ResponseEntity object containing the customerBaseDto object, a null error message, and an OK status code.
     */
    @Override
    public ResponseEntity<?> getByGsmNumber(Long gsmNumber) {
        List<CustomerNumber> customerNumbers = customerNumberRepository.getByNumber(gsmNumber);
        if (customerNumbers.isEmpty()){
            throw new ApplicationException(ErrorsFinal.DATA_NOT_FOUND,
            Map.ofEntries(Map.entry("id", gsmNumber)));
        }
        CustomerNumber customerNumber = customerNumbers.get(0);
        CustomerBaseDto dto = customerMapper.entityToDto(customerNumber.getCustomer());
        dto.setBalance(customerNumber.getBalance());
        dto.setGsmNumber(customerNumber.getGsmNumber());
        return MessageResponse.response(SuccessMessage.SUCCESS_GET, dto, null, HttpStatus.OK);
    }

    /**
     * Retrieves all active customers from the repository and converts them to CustomerBaseDto objects.
     * @return a ResponseEntity object containing a list of CustomerBaseDto objects, a null error message, and an OK status code.
     */
    @Override
    public ResponseEntity<?> getActives() {
        List<Customer> find = customerRepository.findAllByStatus('1');
        List<CustomerBaseDto> dto = customerMapper.entityToDtos(find);
        return MessageResponse.response(SuccessMessage.SUCCESS_GET, dto, null, HttpStatus.OK);
    }

    /**
     * Creates and saves a new customer.
     * @param customerBaseDto A CustomerBaseDto object that contains the details of the customer to be created.
     * @return A ResponseEntity object containing the success message and the created customer details.
     */
    @Override
    public ResponseEntity<?> addCustomer(CustomerBaseDto customerBaseDto) {
        BigDecimal minBalanceLimit = new BigDecimal(100);
        if (customerBaseDto.getBalance().compareTo(minBalanceLimit)<0) {
            throw new ApplicationException(ErrorsFinal.WRONG_INITIAL_BALANCE);
        }
        List<CustomerNumber> customerNumbers=customerNumberRepository.getByNumber(customerBaseDto.getGsmNumber());
        if (!customerNumbers.isEmpty()) {
            throw new ApplicationException(ErrorsFinal.GSM_NUMBER_EXISTS,
            Map.ofEntries(Map.entry("gsmNumber", customerBaseDto.getGsmNumber())));
        }
        Customer customer = customerMapper.dtoToEntity(customerBaseDto);
        Long createdBy = customAuthorization.getUserIdFromToken();
        customer.setCreatedBy(createdBy);
        customerRepository.save(customer);
        CustomerNumber customerNumber= new CustomerNumber();
        customerNumber.setBalance(BigDecimal.ZERO);
        customerNumber.setGsmNumber(customerBaseDto.getGsmNumber());
        customerNumber.setCustomer(customer);
        customerNumber.setCreatedBy(createdBy);
        customerNumberRepository.save(customerNumber);
        CustomerBaseDto posDto = customerMapper.entityToDto(customer);
        posDto.setBalance(customerBaseDto.getBalance());
        posDto.setGsmNumber(customerNumber.getGsmNumber());
        paymentApiService.topUpBalance(customerNumber.getGsmNumber(), customerBaseDto.getBalance());
        return MessageResponse.response(SuccessMessage.SUCCESS_ADD, posDto, null, HttpStatus.OK);
    }

    /**
     * Adds balance gsm number.
     * @param amountBaseDto A AmountBasDto object that contains the details of the amount.
     * @return A ResponseEntity object containing the success message and the updated details.
     */
    @Override
    public ResponseEntity<?> addBalance(AmountBaseDto amountBaseDto, Long gsmNumber) {
        List<CustomerNumber> customerNumbers = customerNumberRepository.getByNumber(gsmNumber);
        if (customerNumbers.isEmpty()){
            throw new ApplicationException(ErrorsFinal.DATA_NOT_FOUND,
            Map.ofEntries(Map.entry("id", gsmNumber)));
        }
        CustomerNumber customerNumber = customerNumbers.get(0);
        customerNumber.setBalance(customerNumber.getBalance().add(amountBaseDto.getAmount()));
        CustomerBaseDto dto = customerMapper.entityToDto(customerNumber.getCustomer());
        customerNumberRepository.save(customerNumber);
        return MessageResponse.response(SuccessMessage.SUCCESS_ADD, dto, null, HttpStatus.OK);
    }

    /**
     * Subtracts balance gsm number.
     * @param amountBaseDto A AmountBasDto object that contains the details of the amount.
     * @return A ResponseEntity object containing the success message and the updated details.
     */
    @Override
    public ResponseEntity<?> subtractBalance(AmountBaseDto amountBaseDto, Long gsmNumber) {
        List<CustomerNumber> customerNumbers = customerNumberRepository.getByNumber(gsmNumber);
        if (customerNumbers.isEmpty()){
            throw new ApplicationException(ErrorsFinal.DATA_NOT_FOUND,
            Map.ofEntries(Map.entry("id", gsmNumber)));
        }
        CustomerNumber customerNumber = customerNumbers.get(0);
        customerNumber.setBalance(customerNumber.getBalance().subtract(amountBaseDto.getAmount()));
        CustomerBaseDto dto = customerMapper.entityToDto(customerNumber.getCustomer());
        customerNumberRepository.save(customerNumber);
        return MessageResponse.response(SuccessMessage.SUCCESS_ADD, dto, null, HttpStatus.OK);
    }

    /**
     * Updates an existing customer with the given ID.
     * @param customerBaseDto A CustomerBaseDto object that contains the details of the customer to be updated.
     * @param id              The ID of the customer to be updated.
     * @return A ResponseEntity object containing the success message and the updated customer details.
     * @throws ApplicationException if the customer with the given ID is not found.
     */
    @Override
    public ResponseEntity<?> updateCustomer(CustomerBaseDto customerBaseDto, Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorsFinal.DATA_NOT_FOUND, Map.ofEntries(Map.entry("id", id), Map.entry("name", "Customer"))));
        customerMapper.updateEntity(customer, customerBaseDto);
        customerRepository.save(customer);
        CustomerBaseDto dto = customerMapper.entityToDto(customer);
        return MessageResponse.response(SuccessMessage.SUCCESS_UPDATE, dto, null, HttpStatus.OK);
    }

    /**
     * Deletes an existing customer with the given ID.
     * @param id The ID of the customer to be deleted.
     * @return A ResponseEntity object containing the success message and the deleted customer details.
     * @throws ApplicationException if the customer with the given ID is not found.
     */
    @Override
    public ResponseEntity<?> deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorsFinal.DATA_NOT_FOUND,
                        Map.ofEntries(Map.entry("id", id), Map.entry("name", "Vəzifəyə"))));

        customerRepository.delete(customer);
        CustomerBaseDto customerBaseDto = customerMapper.entityToDto(customer);
        return MessageResponse.response(SuccessMessage.SUCCESS_DELETE, customerBaseDto, null, HttpStatus.OK);
    }

    @Override
    public CustomerBaseDto getIdAndName(Long id) {
        Optional<Customer> customer = customerRepository.getByIdAndStatus(id, '1'); //.orElseThrow(()-> new ApplicationException(ErrorsFinal.DATA_NOT_FOUND));
        if(customer.isPresent())
            return customerMapper.entityToDto(customer.get());

        return new CustomerBaseDto();
    }

}
