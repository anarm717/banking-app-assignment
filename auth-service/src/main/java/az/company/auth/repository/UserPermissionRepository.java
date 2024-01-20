package az.company.auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import az.company.auth.entity.User;
import az.company.auth.entity.UserPermission;

import java.util.List;
import java.util.Optional;


public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

    List<UserPermission> findByUserId(Long id);

    List<UserPermission> findByUserIdAndStatus(Long userId,Character status);

    List<UserPermission> findByUserIdAndStatusAndAccess(Long userId,Character status,Boolean access);


}
