package az.company.auth.dto.UserDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**

 User data transfer object for representing user information.

 Includes user id, username, password, name, surname, father name, mobile, email, note, status, creation date and creator id.
 */

@Data
public class UserDto extends UserGetDto{

//    private Integer id;

    //    private String idCardSeries;
//    private Integer idCardNumber;

    private String password;

}
