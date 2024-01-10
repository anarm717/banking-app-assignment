package az.company.auth.dto.UserDto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**

 A data transfer object class representing a user update request.
 This class is annotated with Lombok's @Getter and @Setter annotations to generate getter and setter methods for all fields.

 */

@Getter
@Setter
public class UserUpdateDto {

    private String idCardSeries;
    private Long idCardNumber;
    private String username;
    private Short userType;
    private String name;
    private String surname;
    private String fatherName;
    private String status;
    private Long employeeId;
    private Set<Long> roles;
}
