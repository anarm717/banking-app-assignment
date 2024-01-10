package az.company.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 A DTO representing user permissions.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private Boolean access;
    private LocalDateTime createdAt;
    private Long createdBy;
}
