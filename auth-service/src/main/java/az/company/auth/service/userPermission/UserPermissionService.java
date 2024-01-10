package az.company.auth.service.userPermission;



/**

 This interface  provides methods for managing userPermissions.
 */

public interface UserPermissionService {

   boolean deletePermByUserId(Long id, Character status);
}
