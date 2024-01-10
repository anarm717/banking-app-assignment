package az.company.auth.service.permission;

import java.util.List;

import az.company.auth.dto.PermissionDto.*;

/**

 The PermissionService interface provides methods for managing permissions.
 */

public interface PermissionService {


    GetPermByAppRoleIdWithTotalDto getByAppIdAndRoleIds(Long appId, Long roleId);

    /**

     Retrieves a PermissionBaseDto by its ID.
     @param id the ID of the permission to retrieve
     @return the PermissionBaseDto with the specified ID
     */
    PermissionBaseDto getById(Long id);

    /**

     Retrieves all PermissionBaseDtos.
     @return a list of all PermissionBaseDtos
     */
    List<PermissionBaseDto> getAll();

    /**
     Retrieves all PermissionBaseDtos by AppID.
     @param idDto the ID of the AppId and RoleID to retrieve permissions
     @return a list of all PermissionBaseDtos
     */
    PermissionListDto getByAppIdAndRoleIds(PermissionAppRoleIdDto idDto);

    /**

     Inserts a new PermissionBaseDto.
     @param dto the PermissionBaseDto to insert
     @return the inserted PermissionBaseDto
     */
    PermissionBaseDto insert(PermissionBaseDto dto);

    /**

     Deletes a PermissionBaseDto by its ID.
     @param id the ID of the permission to delete
     */
    void deletePermission(Long id);
}
