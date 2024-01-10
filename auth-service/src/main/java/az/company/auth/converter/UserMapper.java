package az.company.auth.converter;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import az.company.auth.dto.RoleDto.RoleNameDto;
import az.company.auth.dto.UserDto.UserDto;
import az.company.auth.dto.UserDto.UserGetDto;
import az.company.auth.dto.UserDto.UserWithRoleDto;
import az.company.auth.entity.Role;
import az.company.auth.entity.User;
import az.company.auth.entity.UserRole;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fuad
 */
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Mapping(target = "password", qualifiedByName = "passwordEncoding")
    public abstract User toEntity(UserDto dto);

    public abstract UserDto toDto(User user);

    public abstract List<UserGetDto> getDtoList(List<User> listUsers);

    public abstract List<UserDto> toDtos(List<User> listUsers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void update(@MappingTarget User user, UserDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void update(@MappingTarget User user, UserGetDto dto);

    @Named("passwordEncoding")
    String passwordEncoding(String password) {
        return passwordEncoder.encode(password);
    }

    @Mapping(target = "roles", source = "userRoles", qualifiedByName = "toRoleNameDtoList")
    public abstract UserWithRoleDto toUserDtoWithRoles(User user);

    public abstract RoleNameDto toRoleNameDto(Role role);

    @Named("toRoleNameDtoList")
    List<RoleNameDto> toRoleNameDtoList(List<UserRole> userRoles) {
        return userRoles.stream()
                .map(userRole -> toRoleNameDto(userRole.getRole()))
                .collect(Collectors.toList());
    }

}
