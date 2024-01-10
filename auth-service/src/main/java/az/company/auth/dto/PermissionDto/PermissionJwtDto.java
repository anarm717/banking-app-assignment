package az.company.auth.dto.PermissionDto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/**

 The PermissionJwtDto class represents a permission object used for authentication and authorization purposes in the system.

 It implements the Spring Security interface GrantedAuthority.

 A permission is a right or privilege granted to a user or group of users to access specific system resources or perform certain actions.

 This class defines the permission ID and permission name of the permission object.

 The permission name is used as the authority string in Spring Security to control access to resources and actions.

 */

@Getter
@Setter
public class PermissionJwtDto implements GrantedAuthority {
    private Integer id;
    private String permissionName;

    @Override
    public String getAuthority() {
        return permissionName;
    }
}
