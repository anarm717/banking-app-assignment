package az.company.app.service;

import java.math.BigDecimal;

import az.company.app.model.CustomerBaseDto;


public interface PaymentApiService {

    /**
     Top up balance by gsmNumber.
     @param dto the data transfer object containing the necessary parameters for topUp balance .
     @return true if the operation is successfully finished, false otherwise.
     */
    CustomerBaseDto topUpBalance(Long gsmNumber, BigDecimal amount);

}
