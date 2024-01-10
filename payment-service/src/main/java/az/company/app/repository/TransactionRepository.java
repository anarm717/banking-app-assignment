package az.company.app.repository;

import az.company.app.entity.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select p from Transaction p")
    List<Transaction> findAll();

    @Query("select  p from Transaction p where p.status = :character")
    List<Transaction> findAllByStatus(Character character);

    @Query("select  p from Transaction p where p.transactionUUId = :transactionUUID and p.transactionType.id=:transactionTypeId")
    List<Transaction> getByTransactionId(String transactionUUID, Long transactionTypeId);

    Optional<Transaction> getByIdAndStatus(Long id, Character status);
}
