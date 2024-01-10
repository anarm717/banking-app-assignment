package az.company.auth.converter;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import az.company.auth.dto.PermissionDto.PermissionIdDto;
import az.company.auth.dto.RoleDto.RoleBaseDto;
import az.company.auth.dto.RoleDto.RoleDto;
import az.company.auth.dto.RoleDto.RolesGetDto;
import az.company.auth.entity.Permission;
import az.company.auth.entity.Role;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author fuad
 */

@Mapper(componentModel = "spring")
public abstract class RoleMapper {
    
    @Autowired
    private PermissionMapper mapper;


    public abstract Role toEntity (RoleBaseDto dto);

    public abstract Role InsertToEntity(RoleDto dto);

//    @Mapping(target = "permission", source = "permissions")
    public abstract RoleBaseDto toDto (Role role);

    public abstract RoleDto toRoleDto (Role role);

//        @Mapping(target = "permission", qualifiedByName = "parentToLong")
    public abstract List<RoleBaseDto> toDtos(List<Role> roles);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntity(@MappingTarget Role entity, RoleBaseDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityRole(@MappingTarget Role entity, RoleDto dto);


//    @Mapping( target = "permissions",qualifiedByName = "permissionIdDto")
//    public abstract RolesGetDto roleToDto(Role role);
//
//    @Named("permissionIdDto")
//    List<PermissionIdDto> permissionIdDto(List<Permission> entity) {
//        return entity.stream().map(mapper::toIdDto).collect(Collectors.toList());
//    }


    @Mapping(target = "permissions", source = "rolePermissions")
    @Mapping(source = "createdBy",target = "createdBy")
    public abstract RolesGetDto roleToDto(Role role);

    @Mapping(target = "id", source = "id")
    public abstract PermissionIdDto toPermissionIdDto(Permission permission);

    @Named("parentToLong")
    public Long parentToLong(Permission parent){
        return parent != null ? parent.getId() : null;
    }

    protected Set<PermissionIdDto> mapPermissions(Set<Permission> permissions) {
        if (permissions == null) {
            return Collections.emptySet();
        }
        return permissions.stream()
                .map(this::toPermissionIdDto)
                .collect(Collectors.toSet());
    }

}
