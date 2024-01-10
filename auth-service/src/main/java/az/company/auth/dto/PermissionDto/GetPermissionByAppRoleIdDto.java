package az.company.auth.dto.PermissionDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPermissionByAppRoleIdDto {
    private Long id;
    private Long parentId;
    private String permissionName;
    private Long appId;
    private boolean check;
    private List<GetPermissionByAppRoleIdDto> children;
    private boolean checkable;

}
