package az.company.app.repository;

import az.company.app.entity.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select p from Transaction p")
    List<Transaction> findAll();

    @Query("select  p from Transaction p where p.status = :character")
    List<Transaction> findAllByStatus(Character character);

    @Query("select  p from Transaction p where p.transactionUUId = :transactionUUID and status='1'")
    List<Transaction> getByTransactionId(String transactionUUID);

    @Query("select  p from Transaction p where p.gsmNumber = :gsmNumber and status='1' order by p.id desc")
    List<Transaction> getByGsmNumber(Long gsmNumber,Pageable pageable);

    @Query("select  count(p) from Transaction p where p.gsmNumber = :gsmNumber and status='1'")
    List<Integer> getTransactionsCountByGsmNumber(Long gsmNumber);

    @Query("select  p from Transaction p where p.transactionUUId = :transactionUUID and p.transactionType.id=:transactionTypeId and status='1'")
    List<Transaction> getByTransactionId(String transactionUUID, Long transactionTypeId);

    @Query("select  sum(p.amount) from Transaction p where p.transactionUUId = :transactionUUID and p.transactionType.id=:transactionTypeId and status='1'")
    List<BigDecimal> getSumAmountByTransactionId(String transactionUUID, Long transactionTypeId);

    Optional<Transaction> getByIdAndStatus(Long id, Character status);
}
