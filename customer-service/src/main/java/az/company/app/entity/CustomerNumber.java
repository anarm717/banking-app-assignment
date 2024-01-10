package az.company.app.entity;

import java.math.BigDecimal;
import java.util.Objects;

import org.hibernate.annotations.SQLDelete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_numbers", schema = "customer")
@SQLDelete(sql = "update customer.customer_numbers set status='0', end_date=current_timestamp where id =?")
public class CustomerNumber extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column
    private Long gsmNumber;

    @Column
    private BigDecimal balance;

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(getId(), customer.getId());
    }

    public CustomerNumber(Long id) {
        this.setId(id);
    }
}
