package az.company.auth.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import az.company.auth.entity.Permission;

import java.util.List;


public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @EntityGraph(value = "Permission.children", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select p from Permission p join p.appId a where a.id = ?1 and p.parent is null")
    List<Permission> findRootPermissionsByAppId( Long appId);

    List<Permission> findByAppId(Long appId);

    @Query("select p.id from Permission p left join p.rolePermissions rp where rp.roleId.id IN (:roleIds) ")
    List<Long> findPermissionByRoleId(List<Long> roleIds);

    @Query("SELECT p FROM Permission p WHERE p.parent.id = :parentId")
    List<Permission> findChildPermissionsByParentId(@Param("parentId") Long parentId);

    @Query("SELECT p FROM Permission p WHERE p.parent.id = :parentId AND p.appId.id = :appId")
    List<Permission> findChildPermissionsByParentIdAndAppId(@Param("parentId") Long parentId, @Param("appId") Long appId);


}
