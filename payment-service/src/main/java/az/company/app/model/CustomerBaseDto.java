package az.company.app.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class CustomerBaseDto{
    private Long id;

    private String name;

    private String surname;

    // private LocalDate birthDate;

    private Long gsmNumber;

    private BigDecimal balance;

}
