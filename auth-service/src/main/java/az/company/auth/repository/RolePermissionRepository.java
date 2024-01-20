package az.company.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import az.company.auth.entity.Role;
import az.company.auth.entity.RolePermission;
import az.company.auth.entity.User;

import java.util.List;
import java.util.Optional;


public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {


    @Query("SELECT rp FROM RolePermission rp WHERE rp.roleId IN :roleIds")
    List<RolePermission> findByRoleIdIn(List<Long> roleIds);


    @Query("select p from RolePermission p where p.roleId = :role")
    List<RolePermission> getByRoleId(Role role);

    @Query("select p from RolePermission p where p.roleId.id = ?1")
    List<RolePermission> findByRoleId(Long id);

    @Query("SELECT p FROM RolePermission p WHERE p.roleId.id = ?1 AND p.permission.id = ?2")
    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId,Long permissionId);

    @Query("SELECT COUNT(rp) FROM RolePermission rp " +
            "WHERE rp.roleId.id = :roleId AND rp.permission.appId.id = :appId AND rp.status = :status")
    long countByAppIdAndRoleIdAndStatus(Long appId, Long roleId, Character status);
}
