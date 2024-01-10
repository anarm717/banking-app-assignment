package az.company.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorizeRequest {

    private String token;
    private String methodName;

}
