package az.company.auth.service.userPermission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import az.company.auth.entity.UserPermission;
import az.company.auth.repository.UserPermissionRepository;

import java.util.List;


/**

 Service implementation for managing user permissions.
 */
@Service
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {

    private final UserPermissionRepository repository;

    /**

     Deletes user permissions by user ID and status.
     @param id The ID of the user whose permissions to delete.
     @param status The status of the permissions to delete.
     @return true if permissions were successfully deleted, false otherwise.
     */
    public boolean deletePermByUserId(Long id, Character status){
        List<UserPermission> userPermission = repository.findByUserId(id);
        repository.deleteAll(userPermission);
        return true;
    }

}
