package az.company.app.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "transactions", schema = "payment")
@SQLDelete(sql = "UPDATE payment.transactions SET status = '0', end_date = CURRENT_TIMESTAMP WHERE id = ?")
public class Transaction extends BaseEntity<Long>{
    @Column(name="transaction_uuid",nullable = false)
    private String transactionUUId;

    @Column(nullable = false)
    private Long gsmNumber;
    
    @Column
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private TransactionType transactionType;

    public Transaction(Long id) {
        this.setId(id);
    }
}
