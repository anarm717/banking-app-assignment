package az.company.auth.dto.RoleDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RoleDto {

    private String roleName;
    private Long createdBy;
    private String roleDesc;
    private List<Long> permissionId = new ArrayList<>();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

}
