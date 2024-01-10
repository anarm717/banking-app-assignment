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
@Table(name = "refund_details", schema = "payment")
@SQLDelete(sql = "UPDATE payment.refund_details SET status = '0', end_date = CURRENT_TIMESTAMP WHERE id = ?")
public class RefundDetails extends BaseEntity<Long>{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_transaction_id")
    private Transaction purchaseTransaction;

    public RefundDetails(Long id) {
        this.setId(id);
    }
}
