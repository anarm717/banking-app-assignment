package az.company.auth.service.roles;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import az.company.auth.converter.RoleMapper;
import az.company.auth.dto.RoleDto.RoleBaseDto;
import az.company.auth.dto.RoleDto.RoleDto;
import az.company.auth.entity.Permission;
import az.company.auth.entity.Role;
import az.company.auth.entity.RolePermission;
import az.company.auth.exception.ApplicationException;
import az.company.auth.exception.error.Errors;
import az.company.auth.repository.PermissionRepository;
import az.company.auth.repository.RolePermissionRepository;
import az.company.auth.repository.RoleRepository;
import az.company.auth.service.rolePermission.RolePermissionService;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for managing roles.
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper mapper;
    private final RolePermissionService rolePermissionService;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;


    /**
     * Get a specific role by its id.
     *
     * @param id the id of the role to retrieve
     * @return the role with the given id as a DTO
     * @throws NullPointerException if no role with the given id is found
     */
    @Override
    public RoleBaseDto getRoleById(Long id) {
        Role entity = roleRepository.findById(id).orElseThrow(() -> new NullPointerException("No Role with this id"));
        return mapper.toDto(entity);
    }

    /**
     * Get all roles
     *
     * @return a list of all roles as DTOs
     */
    @Override
    public List<RoleBaseDto> getAll() {
        List<Role> roles = roleRepository.findByStatus('1');
        return mapper.toDtos(roles);
    }

    /**
     * Create a new role.
     *
     * @param dto the role DTO to be created
     * @return the created role DTO
     */
    @Override
    public RoleDto insert(RoleDto dto) {

        Role role = mapper.InsertToEntity(dto);

        List<RolePermission> rolePermissions = new ArrayList<>();

        for (Long permissionId : dto.getPermissionId()) {
            Permission permission = new Permission(permissionId);
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermission(permission);
            rolePermission.setRoleId(role);
            rolePermission.setAccess(true);
            rolePermissions.add(rolePermission);
        }

        role.setRolePermissions(rolePermissions);
        roleRepository.save(role);

        return dto;
    }

    /**
     * Update an existing role.
     *
     * @param id  the id of the role to be updated
     * @param dto the role DTO with updated information
     * @return the updated role DTO
     * @throws NullPointerException if no role with the given id is found
     */
    @Override
    public RoleDto updateRole(Long id, RoleDto dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No Role with this id!"));

        rolePermissionService.deletePermission(id);

        mapper.updateEntityRole(role, dto);

        // Get existing RolePermission records for this Role
//        List<RolePermission> existingRolePermissions = rolePermissionRepository.getByRoleId(role);

        // Update RolePermission records by replacing old Permission IDs with new ones
        List<RolePermission> updateRolePermissions = new ArrayList<>();

        // Add remaining Permission IDs as new RolePermission records
        for (Long permissionId : dto.getPermissionId()) {
            RolePermission newRolePermission = new RolePermission();
            newRolePermission.setRoleId(role);
            newRolePermission.setAccess(true);
            newRolePermission.setPermission(permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new NotFoundException("No Permission with this id!")));
            updateRolePermissions.add(newRolePermission);
        }

        role.setRolePermissions(updateRolePermissions);
        roleRepository.save(role);

        return mapper.toRoleDto(role);
    }

    /**
     * Delete a role by its id.
     *
     * @param id the id of the role to be deleted
     * @throws NullPointerException if no role with the given id is found
     */
    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findByIdAndStatus(id,'1')
                .orElseThrow(() -> new ApplicationException(Errors.ROLE_NOT_FOUND));
        if (role.getUserRoles().isEmpty()) {
            role.setStatus('0');
            rolePermissionService.deletePermission(id);
            roleRepository.save(role);
        }else
        throw  new IllegalArgumentException("Cannot delete role. There are users with this role.");
    }
    /**
     * Update an existing role.
     *
     * @param fromRoleId the id of the role to copied permissions
     * @param toRoleId   the id of the role to be updated permissions
     * @throws NotFoundException if no role with the given id is found
     */
    @Override
    public void duplicatePermissions(Long fromRoleId, Long toRoleId) {
        Role toRole = roleRepository.findById(toRoleId).orElseThrow(() -> new NotFoundException("ID not found"));
        Role fromRole = roleRepository.findById(fromRoleId).orElseThrow(() -> new NotFoundException("ID not found"));

        List<RolePermission> fromRolePermissions = rolePermissionRepository.getByRoleId(fromRole);

        List<RolePermission> toRolePermissions = new ArrayList<>();
        for (RolePermission fromRolePermission : fromRolePermissions) {
            RolePermission toRolePermission = new RolePermission();
            toRolePermission.setPermission(fromRolePermission.getPermission());
            toRolePermission.setRoleId(toRole);
            toRolePermission.setAccess(true);
            toRolePermissions.add(toRolePermission);
        }

        rolePermissionService.deletePermission(toRoleId);

        rolePermissionRepository.saveAll(toRolePermissions);
    }
}
