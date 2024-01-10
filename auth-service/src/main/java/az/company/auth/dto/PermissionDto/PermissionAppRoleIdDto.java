package az.company.auth.dto.PermissionDto;

import lombok.*;

import java.util.List;

/**
 * @author fuad
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionAppRoleIdDto {

   private Long appId;
   private List<Long> roleId;
}
