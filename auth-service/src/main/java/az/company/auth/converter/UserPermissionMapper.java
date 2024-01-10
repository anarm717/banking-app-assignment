package az.company.auth.converter;

import org.mapstruct.Mapper;

import az.company.auth.dto.UserPermissionDto;
import az.company.auth.entity.UserPermission;

import java.util.List;

/**
 * @author fuad
 */
@Mapper(componentModel = "spring")
public abstract class UserPermissionMapper {

    public abstract UserPermission toEntity (UserPermissionDto dto);

    public abstract UserPermissionDto toDto (UserPermission entity);

    public abstract List<UserPermissionDto> toDtos (List<UserPermission> entity);

}
