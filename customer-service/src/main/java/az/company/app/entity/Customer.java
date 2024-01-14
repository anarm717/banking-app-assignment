package az.company.app.entity;

import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers", schema = "customer")
@SQLDelete(sql = "update customer.customers set status='0', end_date=current_timestamp where id =?")
public class Customer extends BaseEntity<Long> {

    @Column(length = 350)
    private String name;

    @Column(length = 350)
    private String surname;

    @Column
    private LocalDate birthDate;

    @OneToMany(
            mappedBy = "customer",
            cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE },
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @Where(clause = " status = '1' ")
    private List<CustomerNumber> customerNumbers;

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

    public Customer(Long customerId) {
        this.setId(customerId);
    }
}
