package az.company.auth.dto.RoleDto;

import lombok.Data;

import java.util.Set;

import az.company.auth.dto.PermissionDto.PermissionIdDto;

/**
 * Represents a Get DTO class for retrieving role information including the role name,
 * creator ID, role description, and associated permissions.
 */
@Data
public class RolesGetDto {

    private String roleName;
    private Long createdBy;
    private String roleDesc;
    private Set<PermissionIdDto> permissions;

}