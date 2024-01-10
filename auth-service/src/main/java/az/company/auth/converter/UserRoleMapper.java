package az.company.auth.converter;

import org.mapstruct.Mapper;

import az.company.auth.dto.UserDto.UserRolePermissionDto;
import az.company.auth.entity.UserRole;

import java.util.List;

/**
 * @author fuad
 */

@Mapper(componentModel = "spring")
public abstract class  UserRoleMapper {

    public abstract List<UserRolePermissionDto> toDtos (List<UserRole> entity);
}
