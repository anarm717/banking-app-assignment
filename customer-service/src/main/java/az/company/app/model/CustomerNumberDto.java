package az.company.app.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerNumberDto extends BaseDto<Long> {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private Long gsmNumber;

    private BigDecimal balance;
}
