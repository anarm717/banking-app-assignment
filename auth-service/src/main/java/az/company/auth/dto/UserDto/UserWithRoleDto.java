package az.company.auth.dto.UserDto;

import com.fasterxml.jackson.annotation.JsonProperty;

import az.company.auth.dto.RoleDto.RoleNameDto;
import az.company.auth.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class UserWithRoleDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String fatherName;
    private String mobile;
    private String email;
    private String note;
    private Long employeeId;
    private Character status;
    private LocalDateTime createdAt;
    private Long createdBy;
    private List<RoleNameDto> roles;
}
