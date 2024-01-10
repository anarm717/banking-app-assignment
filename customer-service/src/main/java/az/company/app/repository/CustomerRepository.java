package az.company.app.repository;

import az.company.app.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select p from Customer p")
    List<Customer> findAll();

    @Query("select  p from Customer p where p.status = :character")
    List<Customer> findAllByStatus(Character character);

    Optional<Customer> getByIdAndStatus(Long id, Character status);
}
