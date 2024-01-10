package az.company.auth.dto.PermissionDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Here is the key is appId
// Total is simply total

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionTotalDto {
    private Long key;
    private Long total;
}
