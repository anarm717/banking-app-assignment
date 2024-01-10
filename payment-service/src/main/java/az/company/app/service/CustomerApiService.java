package az.company.app.service;

import java.math.BigDecimal;

import az.company.app.model.CustomerBaseDto;


public interface CustomerApiService {

    /**
     Top up balance by gsmNumber.
     @param dto the data transfer object containing the necessary parameters for topUp balance .
     @return true if the operation is successfully finished, false otherwise.
     */
    CustomerBaseDto getCustomerByGsmNumber(Long gsmNumber);

    /**
     Add amount to balance by gsmNumber.
     @param dto the data transfer object containing the necessary parameters for topUp balance .
     @return true if the operation is successfully finished, false otherwise.
     */
    CustomerBaseDto addBalanceByGsmNumber(Long gsmNumber, BigDecimal amount);

    /**
     Subtract amount from balance by gsmNumber.
     @param dto the data transfer object containing the necessary parameters for topUp balance .
     @return true if the operation is successfully finished, false otherwise.
     */
    CustomerBaseDto subtractBalanceByGsmNumber(Long gsmNumber, BigDecimal amount);
}
