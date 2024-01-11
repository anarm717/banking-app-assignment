package az.company.app.repository;

import az.company.app.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select p from Customer p")
    List<Customer> findAll();

    @Query("select  p from Customer p where p.status = :character order by p.id")
    List<Customer> findAllByStatus(Character character, Pageable pageable);

    @Query("select  count(p) from Customer p where p.status = :character")
    List<Integer> getAllCountByStatus(Character character);

    Optional<Customer> getByIdAndStatus(Long id, Character status);
}
