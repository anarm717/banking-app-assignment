package az.company.auth.service.userRole;

/**
 * This interface represents the service for managing user roles.
 */
public interface UserRoleService {

    /**
     * Deletes all roles associated with the given user ID.
     *
     * @param id     the user ID whose roles should be deleted
     * @param status the status of the user
     * @return true if all roles were deleted successfully, false otherwise
     */
    boolean deleteRoleByUserId(Long id, Character status);


    /**
     * Deletes all roles associated with the given user ID.
     *
     * @param userId the user ID whose roles should be deleted
     * @param roleId the role ID whose roles should be deleted
     * @return true if all roles were deleted successfully, false otherwise
     */
    void deleteRoleByUserAndRoleId(Long userId, Long roleId);


}
