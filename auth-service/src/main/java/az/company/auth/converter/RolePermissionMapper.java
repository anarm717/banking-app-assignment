package az.company.auth.converter;

import org.mapstruct.Mapper;

import az.company.auth.dto.RoleDto.RolePermissionDto;
import az.company.auth.entity.RolePermission;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class RolePermissionMapper {

//    public abstract RolePermissionDto toDto(RolePermission rolePermission);
//
//    public abstract RolePermission toEntity(RolePermissionDto dto);

    public abstract List<RolePermissionDto> toRolePermissionDtos(List<RolePermission> entity);

    public abstract List<RolePermission> toEntities(List<RolePermissionDto> dto);

}
