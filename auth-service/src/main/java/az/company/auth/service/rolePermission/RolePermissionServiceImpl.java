package az.company.auth.service.rolePermission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import az.company.auth.entity.RolePermission;
import az.company.auth.repository.RolePermissionRepository;

import java.util.List;

/**
 * @author fuad
 */
@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements RolePermissionService{

    private final RolePermissionRepository rolePermissionRepository;

    /**

     Delete a Role by ID.
     @param roleId the ID of the RolePermission to delete
     */
    @Override
    public void deletePermission(Long roleId) {
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
        if (rolePermissions != null) {
            for(RolePermission r : rolePermissions){
                r.setStatus('0');
            }
            rolePermissionRepository.saveAll(rolePermissions);
        }
    }

}
