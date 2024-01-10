package az.company.auth.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import az.company.auth.dto.PermissionDto.PermissionAppRoleIdDto;
import az.company.auth.dto.PermissionDto.PermissionBaseDto;
import az.company.auth.service.permission.PermissionService;

/**
 * The PermissionController class handles HTTP requests related to permissions in the application.
 * It provides endpoints for retrieving, inserting and deleting permissions.
 */

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PermissionController {

    private final PermissionService services;

    /**
     * Retrieves the permission with the specified ID.
     *
     * @param id the ID of the permission to retrieve
     * @return a ResponseEntity containing the permission if found, or an error message if not found
     */
    @GetMapping("{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(services.getById(id));
    }

    /**
     * Retrieves all permissions.
     *
     * @return a ResponseEntity containing a list of all permissions
     */
    @GetMapping
    ResponseEntity<?> getAllPermissions() {
        return ResponseEntity.ok(services.getAll());
    }

    /**
     * Retrieves all permissions.
     * @param idDto the ID of the roleId and appId to retrieve permissions
     * @return a Permissions containing a list of all permissions
     */

    @PostMapping("/getByAppId/")
    ResponseEntity<?> getByAppId(@Valid @RequestBody PermissionAppRoleIdDto idDto) {
        return ResponseEntity.ok(services.getByAppIdAndRoleIds(idDto));
    }

    @GetMapping("/getByAppIdAndRoleId")
    ResponseEntity<?> getByAppIdAndRoleId(@NotNull @RequestParam Long appId, @RequestParam(value="roleId" ,required = false) Long roleId){
        return  ResponseEntity.ok(services.getByAppIdAndRoleIds(appId,roleId));
    }

    /**
     * Inserts a new permission into the database.
     *
     * @param permissionDto the PermissionBaseDto object representing the permission to insert
     * @return a ResponseEntity containing a success message if the permission is inserted successfully
     */
    @PostMapping
    public ResponseEntity<String> insertPermission(@RequestBody PermissionBaseDto permissionDto) {
        services.insert(permissionDto);
        return ResponseEntity.ok("New permission is inserted!");
    }


    /**
     * Deletes the permission with the specified ID.
     *
     * @param id the ID of the permission to delete
     * @return a ResponseEntity containing a success message if the permission is deleted successfully, or an error message if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePermission(@PathVariable Long id) {
        services.deletePermission(id);
        return ResponseEntity.ok("The user with id: " + id + " is deleted!");
    }
}
