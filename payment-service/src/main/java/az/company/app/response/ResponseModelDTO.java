package az.company.app.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResponseModelDTO<T> extends ResponseModel {
    private T data;
    private T error;

    public ResponseModelDTO(String message, T data, T error) {
        super(message);
        this.data = data;
        this.error = error;
    }
}
