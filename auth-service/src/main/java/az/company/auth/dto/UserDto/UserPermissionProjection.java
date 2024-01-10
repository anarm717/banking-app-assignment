package az.company.auth.dto.UserDto;


/**
 * @author fuad
 */

public interface UserPermissionProjection {

    Long getId();
    boolean getAccess();
    String getStatus();
    String getPermissionName();
}
