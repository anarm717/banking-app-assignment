package az.company.auth.dto.UserDto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto representing userRole
 */


@Getter
@Setter
public class UserRolePermissionDto {

    private Long userId;
    private String permissionName;
    private boolean access;
    private List<Long> roleIds = new ArrayList<>();
    private List<UserPermissionDtoByUserId> permissions = new ArrayList<>();
}
