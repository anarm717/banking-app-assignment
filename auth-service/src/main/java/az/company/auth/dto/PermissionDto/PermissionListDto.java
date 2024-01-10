package az.company.auth.dto.PermissionDto;

import lombok.*;

import java.util.List;

/**
 * @author fuad
 */
@Getter
@Setter
public class PermissionListDto {
    private List<PermissionFullDto> permissionFullDto;
    private List<Long> permissionIds;
    private List<PermissionTotalDto> totalsByAppId;
}
