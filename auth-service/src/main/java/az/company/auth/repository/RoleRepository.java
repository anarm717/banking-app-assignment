package az.company.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import az.company.auth.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {


    @Query("select r from Role r join fetch r.rolePermissions rp")
    List<Role> getAll();

    Optional<Role>findByIdAndStatus(Long roleId,Character status);

    List<Role> findByStatus(Character status);

}
