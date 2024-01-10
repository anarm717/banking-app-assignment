package az.company.auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import az.company.auth.dto.UserDto.UserPermissionProjection;
import az.company.auth.entity.Role;
import az.company.auth.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    Optional<User> findByUsername(String username);

    @Query(value =
            "select ur.role_id " +
                    "from security.users u " +
                    "join security.user_role ur on u.id = ur.user_id " +
                    "where u.id = :userId and ur.status = '1'"
            , nativeQuery = true)
    List<Long> findRolesByUserId(Long userId);

}

