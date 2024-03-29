package az.company.auth.dto.RoleDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**

 A data transfer object (DTO) representing the information needed to update a role.

 This class includes auto-generated getters and setters for its fields, and constructors

 with and without arguments generated by the @AllArgsConstructor and @NoArgsConstructor annotations.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleDto {

    private String roleName;

    private Integer insertUserId;

    private String roleDesc;

    private Set<Long> permissions;


}
