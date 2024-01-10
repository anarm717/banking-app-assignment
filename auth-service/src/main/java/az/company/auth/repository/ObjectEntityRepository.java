package az.company.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import az.company.auth.entity.ObjectEntity;
import az.company.auth.entity.ObjectType;

import java.util.List;
import java.util.Optional;

public interface ObjectEntityRepository extends JpaRepository<ObjectEntity,Long> {

    List<ObjectEntity> findByObjectIdNameAndObjectTypeId(String name, Long id);

}
