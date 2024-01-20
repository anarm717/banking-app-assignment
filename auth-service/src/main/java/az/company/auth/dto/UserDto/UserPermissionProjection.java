package az.company.auth.dto.UserDto;




public interface UserPermissionProjection {

    Long getId();
    boolean getAccess();
    String getStatus();
    String getPermissionName();
}
