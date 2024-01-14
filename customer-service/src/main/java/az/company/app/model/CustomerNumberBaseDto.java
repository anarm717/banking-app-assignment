package az.company.app.model;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerNumberBaseDto extends BaseDto<Long> {
    private Long customerId;

    private Long gsmNumber;

    private BigDecimal balance;
}
