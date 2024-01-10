package az.company.auth.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import az.company.auth.dto.PasswordDto;
import az.company.auth.dto.UserPasswordDto;
import az.company.auth.dto.UserDto.UserDto;
import az.company.auth.dto.UserDto.UserGetDto;
import az.company.auth.dto.UserDto.UserPrivilegesDto;
import az.company.auth.service.userRole.UserRoleService;
import az.company.auth.service.users.UserService;

/**

 The UserController class represents the REST controller for user-related operations.
 This controller is responsible for handling HTTP requests related to users, such as getting, inserting, updating and deleting user information, as well as resetting user passwords and adding user privileges.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final UserRoleService userRoleService;

    /**
     * Retrieves the user by their ID.
     * @param id the ID of the user to retrieve
     * @return the user with the specified ID
     */
    @GetMapping("{id}")
    ResponseEntity<?> getByIdUsers(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Retrieves all users.
     * @return a list of all users
     */
    @GetMapping("/getAll")
    @RolesAllowed("UserSettings1")
    ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok( userService.getAll());
    }


    /**
     * Inserts a new user into the system.
     * @param userDto the DTO containing the user information
     * @return a success message indicating that the user was inserted
     */
    @PostMapping
    public ResponseEntity<String> insertUser(@RequestBody UserDto userDto){
        userService.insertUser(userDto);
        return ResponseEntity.ok("New user is inserted!");
    }

    /**
     * Deletes a user with the specified ID.
     * @param id the ID of the user to delete
     * @return a success message indicating that the user was deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("The user with id: "+id+" is deleted!");
    }

    /**
     * Updates the information of an existing user.
     * @param id the ID of the user to update
     * @param userDto the DTO containing the updated user information
     * @return a success message indicating that the user was updated
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,@RequestBody UserGetDto userDto){
        userService.updateUser(id,userDto);
        return ResponseEntity.ok("The user with id: "+id+" is updated");
    }

    /**
     * Resets the password of a user with the specified ID.
     * @param id the ID of the user whose password is to be reset
     * @param password the DTO containing the new password
     * @return a success message indicating that the user's password was reset
     */
    @PostMapping("/reset/{id}")
    public ResponseEntity<String> resetPassword(@PathVariable Long id,@RequestBody PasswordDto password){
            userService.resetPassword(id, password.getPassword());
            return ResponseEntity.ok("The password of the user with id: " + id + " is resetted");
    }

    /**
     * Resets the password of a user with the specified ID.
     * @param id the ID of the user whose password is to be reset
     * @param password the DTO containing the new password
     * @return a success message indicating that the user's password was reset
     */
    @PostMapping("/reset")
    public ResponseEntity<String> resetUserPassword(@RequestBody UserPasswordDto userPassword){
            Long userId = userService.resetUserPassword(userPassword.getOldPassword(),userPassword.getNewPassword());
            if (userId==0) {
                return ResponseEntity.ok("Old and new password is equal"); 
            } else if (userId==-1){
                return ResponseEntity.ok("Old password is not correct"); 
            }
            else {
                return ResponseEntity.ok("The password of the user with id: " + userId + " is resetted");
            }
    }

//    @PostMapping("/addRole/{userId}/{roleId}")
//    public ResponseEntity<String> addRole(@PathVariable Long userId, @PathVariable Long roleId){
//        userService.addRole(userId,roleId);
//        return ResponseEntity.ok("The role with id: "+roleId+" was added to user with id: "+userId);
//    }

    /**
     * Adds privileges to a user.
     * @param dto the DTO containing the user ID and the list of privileges to be added
     * @return a success message indicating that the privileges were added to the user
     */
    @PutMapping("addPrivileges")
    ResponseEntity<?> addPrivileges(@RequestBody UserPrivilegesDto dto) {
        userService.addPrivileges(dto);
        return ResponseEntity.ok("The privileges of the user was added");
    }


    /**
     * Returns a ResponseEntity containing a list of roles for the user with the specified id
     * @param id The id of the user to retrieve roles for
     */
    @GetMapping("/getRoleAndPermissionByUserId/{id}")
    public ResponseEntity<?> getRoleAndPermissionByUserId(@PathVariable Long id){
        return ResponseEntity.ok(userService.getRoleAndPermissionByUserId(id));
    }

    /**
     * Deletes a user with the specified ID.
     * @param userId the User ID of the user to delete
     * @param roleId the Role ID of the user to delete
     * @return a success message indicating that the user was deleted
     */
    @DeleteMapping("/deleteUserRole")
    public ResponseEntity<String> deleteUserRoleByUserIdAndRoleId(@RequestParam Long userId, @RequestParam Long roleId){
        userRoleService.deleteRoleByUserAndRoleId(userId,roleId);
        return ResponseEntity.ok("The user with id: "+userId+" and "+roleId+" is deleted!");
    }

    @PutMapping("/duplicateUserRolePermission")
    public ResponseEntity<?> duplicateRolePermission(@RequestParam Long fromUserId, @RequestParam Long toUserId){
        userService.duplicateUserPrivileges(fromUserId, toUserId);
        return ResponseEntity.ok("The permissions and roles with userId: " + fromUserId + " to " + toUserId + " is duplicated");
    }


}