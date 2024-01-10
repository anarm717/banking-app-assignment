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
public class GetPermByAppRoleIdWithTotalDto {
    List<GetPermissionByAppRoleIdDto> GetPermissionByAppRoleIdDto;
    List<PermissionTotalDto> permissionTotalDtos;
}
