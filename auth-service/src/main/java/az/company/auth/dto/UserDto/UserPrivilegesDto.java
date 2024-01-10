package az.company.auth.dto.UserDto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import az.company.auth.dto.PermissionDto.PermissionPrivilegesDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**

 A data transfer object (DTO) class that represents user privileges information, including the IDs of the permissions

 and roles that the user has, as well as whether the user has access to those permissions and roles or not.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivilegesDto {

    private List<PermissionPrivilegesDto> permissions;
    private List<Long> roleIds;
    private Long userId;
//    @JsonIgnore
//    private boolean access;
}
