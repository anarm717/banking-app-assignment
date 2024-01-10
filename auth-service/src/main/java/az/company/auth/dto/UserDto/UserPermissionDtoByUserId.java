package az.company.auth.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionDtoByUserId {
    private Long id;
    private boolean access;
    private Character status;
    private String permissionName;
    private String fullName;
}
