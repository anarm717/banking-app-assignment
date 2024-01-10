package az.company.auth.dto.PermissionDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

import az.company.auth.dto.BaseDto;

/**

 This class represents the DTO for permissions containing basic information.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto extends BaseDto<Integer> {

    private Short appType;
    private Integer permissionType;
    private Integer parentId;
    // private String permission_name_id
    //caption
    private String permissionName;
    private Map<String, String> property;
    private Boolean isActive;
    private String description;
    private Boolean required;


}

