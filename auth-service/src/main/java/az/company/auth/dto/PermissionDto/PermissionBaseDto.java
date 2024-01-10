package az.company.auth.dto.PermissionDto;

import lombok.*;

import java.time.LocalDateTime;

import az.company.auth.entity.Base;

/**

 This class represents the base DTO for permissions containing basic information.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionBaseDto extends Base<Long> {

    private String permissionName;
    private String description;
    private LocalDateTime createdAt;
    private Long createdBy;
}
