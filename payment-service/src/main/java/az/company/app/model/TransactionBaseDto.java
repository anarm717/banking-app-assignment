package az.company.app.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionBaseDto {

    private String transactionUUId;

    private Long gsmNumber;

    private BigDecimal amount;

    private String transactionType;

    private LocalDateTime createdAt;

}
