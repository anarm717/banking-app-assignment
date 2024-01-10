package az.company.auth.dto.PermissionDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author fuad
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionPrivilegesDto {

    private Long permissionIds;
    private boolean access;

}
