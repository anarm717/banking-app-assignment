package az.company.auth.service.userRole;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import az.company.auth.entity.*;
import az.company.auth.repository.RolePermissionRepository;
import az.company.auth.repository.UserPermissionRepository;
import az.company.auth.repository.UserRepository;
import az.company.auth.repository.UserRoleRopository;

import java.util.List;
import java.util.Optional;

/**

 Service interface for managing user roles.
 */
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService{

    private final UserRoleRopository userRoleRopository;
    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    /**

     Deletes all user roles for a given user ID and status.
     @param id The ID of the user whose roles should be deleted.
     @param status The status of the user.
     @return True if the operation was successful, false otherwise.
     */
    @Override
    public boolean deleteRoleByUserId(Long id, Character status) {
        List<UserRole> userRoles = userRoleRopository.findByUserId(id);
        userRoleRopository.deleteAll(userRoles);
        return false;
    }

    /**

     Deletes all user roles for a given user ID and status.
     @param userId The user ID of the user whose user and role should be deleted.
     @param roleId The role ID of the user whose user and role should be deleted.
     @return True if the operation was successful, false otherwise.
     */
    @Override
    public void deleteRoleByUserAndRoleId(Long userId, Long roleId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            UserRole userRole = userRoleRopository.findByUserIdAndRoleIdAndStatus(userId, roleId, '1')
                    .orElseThrow(() -> new NotFoundException("There is no relation with this role and user"));

            userRole.setStatus('0');
            userRoleRopository.save(userRole);

            List<UserPermission> userPermissions = userPermissionRepository.findByUserIdAndStatusAndAccess(userId, '1', false);
            List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);

            rolePermissions.forEach(rp -> {
                boolean found = userPermissions.stream()
                        .anyMatch(up -> up.getPermission() == rp.getPermission());
                if (found) {
                    userPermissions.stream()
                            .filter(up -> up.getPermission() == rp.getPermission())
                            .forEach(up -> {
                                up.setStatus('0');
                                userPermissionRepository.save(up);
                            });
                }
            });
        }

    }


