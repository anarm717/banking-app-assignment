package az.company.auth.dto.RoleDto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fuad
 */

@Data
public class RolePermissionDto {

    private Long userIds;
    private String permissionName;
    private Boolean access;
    private List<Long> roleIds = new ArrayList<>();
    private List<Long> permissionId = new ArrayList<>();

}
