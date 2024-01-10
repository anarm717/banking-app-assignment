package az.company.auth.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

/**

 A generic data transfer object representing a base object with an ID of type T.

 Provides a constructor for initializing the ID.

 @param <T> the type of the ID.
 */

@Data
@NoArgsConstructor
public class BaseDto<T>{

    public BaseDto(T id){
        this.id = id;
    }

    private T id;

}
