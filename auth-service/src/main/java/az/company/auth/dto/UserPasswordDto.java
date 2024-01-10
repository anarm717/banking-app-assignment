package az.company.auth.dto;

import lombok.Getter;
import lombok.Setter;

/**

 A simple data transfer object (DTO) class for representing a password.
 Provides getters and setters for the password field.
 */

@Getter
@Setter
public class UserPasswordDto {
    private String oldPassword;
    private String newPassword;
}
