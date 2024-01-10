package az.company.auth.dto.RoleDto;

import com.fasterxml.jackson.annotation.JsonProperty;

import az.company.auth.entity.Base;
import az.company.auth.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**

 The RoleBaseDto class represents the data transfer object for Role entity.
 It contains information related to a Role such as the Role name, description,
 creation date, and creator. This class extends the Base class which contains
 the id of the Role.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleBaseDto extends Base<Long> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String roleName;
    private String roleDesc;
    private Character status;
    private LocalDateTime createdAt;
    private Long createdBy;
    private List<Permission> permission;

}
