package az.company.auth.converter;


import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import az.company.auth.dto.PermissionDto.PermissionBaseDto;
import az.company.auth.dto.PermissionDto.PermissionFullDto;
import az.company.auth.dto.PermissionDto.PermissionIdDto;
import az.company.auth.entity.App;
import az.company.auth.entity.Permission;

import java.util.List;

/**
 * @author fuad
 */
@Mapper(componentModel = "spring")
public abstract class PermissionMapper {


    public abstract Permission toEntity(PermissionBaseDto dto);

    public abstract PermissionIdDto toPermissionIdDto(Permission permission);
    public abstract List<PermissionIdDto> toPermissionIdDtos(List<Permission> permission);
    public abstract PermissionBaseDto toDto(Permission permission);

    public abstract List<PermissionBaseDto> toBaseDtos(List<Permission> permissions);

    @Named("forFull")
    @Mapping(target = "appId", qualifiedByName = "appIdToLong")
    @Mapping(target = "parent", qualifiedByName = "parentToLong")
    @Mapping(source = "permissionName", target = "fullName")
    public abstract PermissionFullDto toFullDto(Permission permission);

    @IterableMapping(qualifiedByName = "forFull")
    public abstract List<PermissionFullDto> toFullDtos(List<Permission> permissions);

    @Named("parentToLong")
    public Long parentToLong(Permission parent){
        return parent != null ? parent.getId() : null;
    }

    @Named("appIdToLong")
    public Long appIdToLong(App appId){
        return appId != null ? appId.getId() : null;
    }



}
