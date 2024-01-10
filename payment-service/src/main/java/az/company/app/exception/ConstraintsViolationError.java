package az.company.app.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstraintsViolationError {
    private String property;
    private String message;
}
