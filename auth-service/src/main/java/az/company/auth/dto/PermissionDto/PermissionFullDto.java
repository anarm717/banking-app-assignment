package az.company.auth.dto.PermissionDto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionFullDto extends PermissionBaseDto {

    private Long appId;
    private Long parent;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<PermissionFullDto> children = new ArrayList<>();
    private boolean disabled;
    private String fullName;

}
