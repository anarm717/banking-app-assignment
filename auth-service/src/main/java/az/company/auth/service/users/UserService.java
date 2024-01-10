package az.company.auth.service.users;

import java.util.List;

import az.company.auth.dto.UserDto.*;

/**

 Interface for User related operations.
 */
public interface UserService {

    /**

     Get a user by their ID.
     @param id The ID of the user to retrieve.
     @return The UserDto object containing the user's data.
     */
    UserDto getUserById(Long id);

    String getUsernameByUserId(Long id);

    /**

     Get all roles associated with a given user ID.
     @param id The ID of the user to retrieve roles for.
     @return A List of UserRolePermissionDto objects containing the user's roles.
     */
    UserRolePermissionDto getRoleAndPermissionByUserId(Long id);

    /**

     Get all users.
     @return A List of UserGetDto objects containing data for all users.
     */
    List<UserWithRoleDto>  getAll();

    /**

     Add a new user.
     @param dto The UserDto object containing the user's data to be inserted.
     @return The UserDto object containing the user's data after insertion.
     */
    UserDto insertUser(UserDto dto);

    /**

     Add privileges to a user.
     @param dto The UserPrivilegesDto object containing the user's privileges to be added.
     @return The UserPrivilegesDto object containing the user's updated privileges.
     */
    UserPrivilegesDto addPrivileges(UserPrivilegesDto dto);

    /**

     Reset a user's password.
     @param id The ID of the user whose password is to be reset.
     @param password The new password to set.
     */
    void resetPassword(Long id, String password);

        /**

     Reset a user's password.
     @param id The ID of the user whose password is to be reset.
     @param password The new password to set.
     */
    Long resetUserPassword(String oldPassword, String newPassword);


    /**

     Update a user's information.
     @param id The ID of the user to update.
     @param dto The UserGetDto object containing the updated user information.
     @return The UserGetDto object containing the user's updated data.
     */
    UserGetDto updateUser(Long id, UserGetDto dto);

    /**

     Delete a user.
     @param id The ID of the user to delete.
     @return A boolean indicating whether or not the deletion was successful.
     */
    boolean deleteUser(Long id);

    /**

     Duplicate a User Privileges by role ID.
     @param fromUserId the ID of the User to which I want to get User Privileges
     @param toUserId the ID of the User to duplicated
     */
     void duplicateUserPrivileges(Long fromUserId, Long toUserId);
}
