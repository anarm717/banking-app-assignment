package az.company.app.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "transaction_types", schema = "payment")
@SQLDelete(sql = "UPDATE payment.transaction_types SET status = '0', end_date = CURRENT_TIMESTAMP WHERE id = ?")
public class TransactionType extends BaseEntity<Long>{

    @Column(nullable = false)
    private String name;

    public TransactionType(Long id) {
        this.setId(id);
    }
}
