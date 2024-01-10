package az.company.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import az.company.auth.entity.Role;
import az.company.auth.entity.User;
import az.company.auth.entity.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * @author fuad
 */
public interface UserRoleRopository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserId(Long userId);

    Optional<UserRole> findByUserIdAndRoleIdAndStatus(Long userId,Long roleId,Character status);


}
