package az.company.auth.service.roles;

import java.util.List;

import az.company.auth.dto.RoleDto.RoleBaseDto;
import az.company.auth.dto.RoleDto.RoleDto;

/**

 This interface provides methods to retrieve, create, update and delete roles.
 */
public interface RoleService {

    /**

     Retrieve a Role by ID.
     @param id the ID of the Role to retrieve
     @return the Role with the specified ID
     */
    RoleBaseDto getRoleById(Long id);


    /**

     Retrieve all Roles.
     @return a list of all Roles
     */
    List<RoleBaseDto> getAll();

    /**

     Create a new Role.
     @param dto the RoleDto representing the new Role to create
     @return the newly created Role
     */
    RoleDto insert(RoleDto dto);

    /**

     Update a Role by ID.
     @param id the ID of the Role to update
     @param dto the RoleDto representing the updated Role
     @return the updated Role
     */
    RoleDto updateRole(Long id, RoleDto dto);


    /**

     Delete a Role by ID.
     @param id the ID of the Role to delete
     */
    void deleteRole(Long id);


    /**

     Duplicate a permissions by role ID.
     @param fromRoleId the ID of the Role to which I want to get permissions
     @param toRoleId the ID of the Role to duplicated
     */
    void duplicatePermissions (Long fromRoleId, Long toRoleId);
}
