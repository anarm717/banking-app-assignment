package az.company.app.repository;

import az.company.app.entity.RefundDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundDetailsRepository extends JpaRepository<RefundDetails, Long> {

    @Query("select p from RefundDetails p")
    List<RefundDetails> findAll();

    @Query("select  p from RefundDetails p where p.status = :character")
    List<RefundDetails> findAllByStatus(Character character);

    @Query("select  p from RefundDetails p where p.transaction.id = :transactionId")
    List<RefundDetails> getByTransactionId(Long transactionId);

    Optional<RefundDetails> getByIdAndStatus(Long id, Character status);
}
