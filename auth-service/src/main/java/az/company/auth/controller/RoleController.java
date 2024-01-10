package az.company.auth.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import az.company.auth.dto.RoleDto.RoleDto;
import az.company.auth.service.rolePermission.RolePermissionService;
import az.company.auth.service.roles.RoleService;

/**
 * The RoleController class is a Spring Boot REST API controller that handles HTTP requests and responses for managing roles.
 * The class provides CRUD (Create, Read, Update, Delete) operations for roles.
 */
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;
    private final RolePermissionService rolePermissionService;

    /**
     * Returns a ResponseEntity containing the RoleBaseDto with the specified id
     * @param id The id of the role to retrieve
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getRoleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    /**
     * Returns a ResponseEntity containing a list of all roles
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(roleService.getAll());
    }

//    /**
//     * Returns a ResponseEntity containing a list of roles for the user with the specified id
//     * @param id The id of the user to retrieve roles for
//     */
//    @GetMapping("/getRolesByUserId/{id}")
//    public ResponseEntity<?> getRolesByUserId(@PathVariable Long id) {
//        return ResponseEntity.ok(userService.getRoleAndPermissionByUserId(id));
//    }

    /**
     * Inserts a new RoleBaseDto into the database and returns the inserted RoleBaseDto
     * @param dto The RoleBaseDto to insert into the database
     * @return The inserted RoleBaseDto
     */
    @PostMapping
    public RoleDto insertRoles(@RequestBody RoleDto dto) {
        return roleService.insert(dto);
    }


    /**
     * Deletes the role with the specified id from the database
     * @param id The id of the role to delete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok("The user with id: " + id + " is deleted");
    }

    /**
     * Updates the role with the specified id with the data in the specified RoleBaseDto
     * @param id  The id of the role to update
     * @param dto The RoleBaseDto containing the updated data
     * @return ResponseEntity<String> indicating that the role with the specified id has been updated
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody RoleDto dto) {
        roleService.updateRole(id, dto);
        return ResponseEntity.ok("The user with id: " + id + " is updated");
    }


    /**
     * Duplicates the role permissions with the specified ids with the roleId
     * @param fromRoleId the ID of the Role to which I want to get permissions
     * @param toRoleId the ID of the Role to duplicated
     * @return ResponseEntity<String> indicating that the role with the specified id has been updated
     */
    @PutMapping("/duplicateRolePermission")
    public ResponseEntity<?> duplicateRolePermission(@RequestParam Long fromRoleId, @RequestParam Long toRoleId){
        roleService.duplicatePermissions(fromRoleId, toRoleId);
        return ResponseEntity.ok("The permissions with roleId: " + fromRoleId + " to " + toRoleId + " is duplicated");
    }
}
