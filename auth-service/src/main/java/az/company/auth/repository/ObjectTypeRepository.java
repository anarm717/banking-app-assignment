package az.company.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import az.company.auth.entity.ObjectEntity;
import az.company.auth.entity.ObjectType;

import java.util.Optional;

public interface ObjectTypeRepository extends JpaRepository<ObjectType,Long> {

    Optional<ObjectType> findById(Integer id);

}
