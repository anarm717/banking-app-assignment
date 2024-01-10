package az.company.auth.dto.UserDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**

 UserGetDto is a data transfer object class that represents a user with reduced information
 used for displaying user data. It includes fields for the user's ID, username, name, surname,
 fatherName, mobile, email, note, status, createdAt, and createdBy.
 The class uses the Lombok's @Data annotation to automatically generate the getters and setters
 for all fields. The @JsonProperty(access = JsonProperty.Access.READ_ONLY) annotation is used
*/

@Data
public class UserGetDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String fatherName;
    private String mobile;
    private String email;
    private String note;
    private Character status;
    private LocalDateTime createdAt;
    private Long createdBy;
    private Long employeeId;

}
