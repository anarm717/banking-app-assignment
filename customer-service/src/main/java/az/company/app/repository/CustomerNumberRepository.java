package az.company.app.repository;

import az.company.app.entity.CustomerNumber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerNumberRepository extends JpaRepository<CustomerNumber, Long> {

    @Query("select p from CustomerNumber p")
    List<CustomerNumber> findAll();

    @Query("select  p from CustomerNumber p where p.status = :character")
    List<CustomerNumber> findAllByStatus(Character character);

    @Query("select  p from CustomerNumber p left join fetch Customer where p.gsmNumber = :gsmNumber")
    List<CustomerNumber> getByNumber(Long gsmNumber);

    Optional<CustomerNumber> getByIdAndStatus(Long id, Character status);
}
