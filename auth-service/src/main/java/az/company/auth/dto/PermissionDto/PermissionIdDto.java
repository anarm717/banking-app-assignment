package az.company.auth.dto.PermissionDto;


import az.company.auth.dto.BaseDto;

/**

 The PermissionIdDto class represents a DTO (Data Transfer Object) for a permission entity with only an ID field.
 This class inherits the BaseDto class to have the basic properties of a DTO.
 The PermissionIdDto is used for transferring only the ID of the permission entity between layers of the application.
 The class does not contain any additional fields other than the inherited ID field.
 */

public class PermissionIdDto extends BaseDto<Long> {
}
