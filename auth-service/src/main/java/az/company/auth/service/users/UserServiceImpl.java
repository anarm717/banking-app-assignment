package az.company.auth.service.users;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import az.company.auth.config.CustomAuthorization;
import az.company.auth.converter.UserMapper;
import az.company.auth.dto.PermissionDto.PermissionPrivilegesDto;
import az.company.auth.dto.UserDto.*;
import az.company.auth.entity.*;
import az.company.auth.exception.ApplicationException;
import az.company.auth.exception.error.Errors;
import az.company.auth.repository.*;
import az.company.auth.service.userPermission.UserPermissionService;
import az.company.auth.service.userRole.UserRoleService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of UserService interface methods that defines methods for managing user-related operations.
 */

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final CustomAuthorization customAuthorization;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final UserPermissionRepository userPermissionRepository;
    private final UserRoleRopository userRoleRopository;

    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    private final UserPermissionService userPermissionService;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    /**
     * Returns the user information based on the user ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user information in the form of a UserDto object.
     * @throws NullPointerException if the specified user ID is not found.
     */
    @Override
    public UserDto getUserById(Long id) {
        User entity = userRepository.findById(id).orElseThrow(() -> new NullPointerException("not found id"));
        return userMapper.toDto(entity);
    }

    @Override
    public String getUsernameByUserId(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ApplicationException(Errors.USERNAME_NOT_FOUND));

        return user.getUsername();
    }

    /**
     * Returns the UserRoles associated with the specified user ID.
     *
     * @param id The ID of the user whose roles to retrieve.
     * @return The list of UserRoles in the form of UserRolePermissionDto objects.
     */
    @Override
    public UserRolePermissionDto getRoleAndPermissionByUserId(Long id) {
        UserRolePermissionDto result = new UserRolePermissionDto();

        List<Long> roles = userRepository.findRolesByUserId(id);
        List<UserPermissionDtoByUserId> userPermissionDtoByUserId = new ArrayList<>();
        List<UserPermission> userPermissions = userPermissionRepository.findByUserIdAndStatus(id,'1');

        for(UserPermission up : userPermissions){
            UserPermissionDtoByUserId userPerm = new UserPermissionDtoByUserId();
            userPerm.setPermissionName(up.getPermission().getPermissionName());
            userPerm.setId(up.getPermission().getId());
            userPerm.setStatus(up.getStatus());
            userPerm.setAccess(up.isAccess());
            if(up.getPermission().getParent()!=null) {
                userPerm.setFullName(up.getPermission().getParent().getPermissionName() + " " + up.getPermission().getPermissionName());
            }else {
                userPerm.setFullName(up.getPermission().getPermissionName());
            }

            userPermissionDtoByUserId.add(userPerm);
        }

        result.setUserId(id);
        result.setRoleIds(roles);
        result.setPermissions(userPermissionDtoByUserId);

        return result;
    }


    /**
     * Returns a list of all users.
     *
     * @return The list of users in the form of UserGetDto objects.
     */
    @Override
    public List<UserWithRoleDto> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toUserDtoWithRoles).collect(Collectors.toList());
    }

    /**
     * Creates a new user based on the specified UserDto object.
     *
     * @param dto The UserDto object containing the user information to create.
     * @return The UserDto object that was created.
     */
    @Override
    public UserDto insertUser(UserDto dto) {
        User entity = userMapper.toEntity(dto);
        entity.setStatus('1');
        userRepository.save(entity);
        return dto;
    }


    /**
     * Adds the specified privileges to the user identified by the UserPrivilegesDto object.
     *
     * @param dto The UserPrivilegesDto object containing the user ID and privileges to add.
     * @return The UserPrivilegesDto object that was updated.
     */
    @Override
    public UserPrivilegesDto addPrivileges(UserPrivilegesDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Map<Long, PermissionPrivilegesDto> permissionMap = new HashMap<>();

        for (PermissionPrivilegesDto permission : dto.getPermissions()) {
            long permissionId = permission.getPermissionIds();
            if (!permissionMap.containsKey(permissionId)) {
                permissionMap.put(permissionId, permission);
            }
        }

        List<PermissionPrivilegesDto> permissionPrivilegesDtos = new ArrayList<>(permissionMap.values());


        List<UserPermission> userPermissions = new ArrayList<>();
        List<UserRole> userRoles = userRoleRopository.findByUserId(dto.getUserId());

        List<Long> roles = dto.getRoleIds();
        List<UserRole> updatedUserRoles = new ArrayList<>();

        for (UserRole ur : userRoles) {
            if (roles.contains(ur.getRole().getId())) {
                ur.setStatus('1');
            } else {
                ur.setStatus('0');
                updatedUserRoles.add(ur);
            }
            userRoleRopository.save(ur);
        }

        userRoles.addAll(updatedUserRoles);

        for(Long r : roles){
            if(userRoles.stream().noneMatch(up->up.getRole().getId().equals(r))) {
                Role role = roleRepository.findById(r)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found!"));
                UserRole userRole = new UserRole();
                userRole.setUser(userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("User not found!")));
                userRole.setRole(role);
                userRole.setStatus('1');
                userRoleRopository.save(userRole);
            }
        }

        List<RolePermission> rolePermissions = new ArrayList<>();

        List<Long> permissions2 = new ArrayList<>();
        List<Long> verifiedPermissions = new ArrayList<>();
        List<PermissionPrivilegesDto> permissionPrivilegesDtos1 = new ArrayList<>();

        for (Long userRole : dto.getRoleIds()) {
            List<RolePermission> permissions = rolePermissionRepository.findByRoleId(userRole);
            rolePermissions.addAll(permissions);
        }

        for(RolePermission rolePermission : rolePermissions){
            permissions2.add(rolePermission.getPermission().getId());
        }


        for(PermissionPrivilegesDto  pd : permissionPrivilegesDtos){
            if(permissions2.contains(pd.getPermissionIds())&&pd.isAccess()==false){
                permissionPrivilegesDtos1.add(new PermissionPrivilegesDto(pd.getPermissionIds(),pd.isAccess()));
                verifiedPermissions.add(pd.getPermissionIds());
            }else if(!permissions2.contains(pd.getPermissionIds())&&pd.isAccess()==true){
                permissionPrivilegesDtos1.add(new PermissionPrivilegesDto(pd.getPermissionIds(),pd.isAccess()));
                verifiedPermissions.add(pd.getPermissionIds());
            }
        }

        List<UserPermission> userPermissionList = userPermissionRepository.findByUserId(user.getId());

        for (UserPermission userPermission : userPermissionList) {
            if (!verifiedPermissions.contains(userPermission.getPermission().getId())) {
                userPermission.setAccess(permissionPrivilegesDtos.stream()
                        .filter(dto1 -> dto1.getPermissionIds().equals(userPermission.getPermission().getId()))
                        .map(PermissionPrivilegesDto::isAccess)
                        .findFirst()
                        .orElse(false));
                userPermission.setStatus('0');
                userPermissionRepository.save(userPermission);
            } else {
                userPermission.setAccess(permissionPrivilegesDtos.stream()
                        .filter(dto1 -> dto1.getPermissionIds().equals(userPermission.getPermission().getId()))
                        .map(PermissionPrivilegesDto::isAccess)
                        .findFirst()
                        .orElse(false));
                userPermission.setStatus('1');
                userPermissions.add(userPermission);
                userPermissionRepository.save(userPermission);
            }
        }



        for (Long p : verifiedPermissions) {
            if (userPermissions.stream().noneMatch(up -> up.getPermission().getId().equals(p))) {
                Permission permission = permissionRepository.findById(p)
                        .orElseThrow(() -> new NotFoundException("Permission not found"));

                boolean access = permissionPrivilegesDtos1.stream()
                        .filter(dtos -> dtos.getPermissionIds().equals(p))
                        .map(PermissionPrivilegesDto::isAccess)
                        .findFirst()
                        .orElse(true);

                UserPermission userPermission = new UserPermission(user, permission);
                userPermission.setAccess(access);
                userPermission.setStatus('1');
                userPermissionRepository.save(userPermission);
            }
        }

        return dto;
    }



    /**
     * Resets the password of a user with the given id.
     *
     * @param id       the id of the user whose password is to be reset.
     * @param password the new password to be set for the user.
     */
    public void resetPassword(Long id, String password) {
        User user = userRepository.findById(id).get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

    }

    /**
     * Resets the password of a user with the given id.
     *
     * @param oldPassword       the id of the user whose password is to be reset.
     * @param newPassword the new password to be set for the user.
     */
    public Long resetUserPassword(String oldPassword, String newPassword) {
        if (oldPassword.equals(newPassword)) {
            return (long)0;
        }
        Long userId = customAuthorization.getUserIdFromToken();
        User user = userRepository.findById(userId).get();
        if (user.getStatus()=='1'&&new BCryptPasswordEncoder().matches(oldPassword, user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return userId;
        }
        
        return (long)-1;
    }


    /**
     * Updates the user information of the user with the given id.
     *
     * @param id  the id of the user to be updated.
     * @param dto the DTO object containing the updated user information.
     * @return the DTO object of the updated user.
     * @throws NullPointerException if the user with the given id is not found.
     */
    @Override
    public UserGetDto updateUser(Long id, UserGetDto dto) {
        User entity = userRepository.findById(id).orElseThrow(() -> new NotFoundException("id not found"));
        userMapper.update(entity, dto);
        userRepository.save(entity);
        return dto;
    }

    /**
     * Deletes the user with the given user id along with all its associated roles and permissions.
     *
     * @param userId the id of the user to be deleted.
     * @return true if the user is deleted successfully, false otherwise.
     * @throws NullPointerException if the user with the given id is not found.
     */
    @Override
    public boolean deleteUser(Long userId) {
        User entity = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(" not found"));

        userRoleService.deleteRoleByUserId(userId, '1');

        userPermissionService.deletePermByUserId(userId, '1');

        userRepository.delete(entity);
        return true;
    }


    /**
     * Update an existing role.
     *
     * @param fromUserId the id of the user to copied roles and permissions
     * @param toUserId   the id of the user to be updated roles and permissions
     * @throws NotFoundException if no role with the given id is found
     */
    @Override
    public void duplicateUserPrivileges(Long fromUserId, Long toUserId) {
        User fromUser = userRepository.findById(fromUserId).orElseThrow(() -> new NotFoundException(" User not found"));
        User toUser = userRepository.findById(toUserId).orElseThrow(() -> new NotFoundException(" User not found"));

        UserRolePermissionDto fromUserRolePermissionDto = getRoleAndPermissionByUserId(fromUserId);

        userRoleService.deleteRoleByUserId(toUserId, '1');
        // copy user  role
        for (Long roleId : fromUserRolePermissionDto.getRoleIds()) {
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new NotFoundException("Role not found with id: " + roleId));
            UserRole newRole = new UserRole();
            newRole.setRole(role);
            newRole.setUser(toUser);
            userRoleRopository.save(newRole);
        }


        userPermissionService.deletePermByUserId(toUserId, '1');
        // copy user permission
        for (UserPermissionDtoByUserId userPermission : fromUserRolePermissionDto.getPermissions()) {
            Permission permission = permissionRepository.findById(userPermission.getId()).orElseThrow(() -> new NotFoundException("Permission not found with id: " + userPermission.getId()));
            UserPermission newUserPermission = new UserPermission();
            newUserPermission.setUser(toUser);
            newUserPermission.setPermission(permission);
            newUserPermission.setAccess(userPermission.isAccess());
            userPermissionRepository.save(newUserPermission);
        }

    }
}