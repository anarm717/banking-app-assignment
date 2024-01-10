package az.company.auth.service.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import az.company.auth.converter.PermissionMapper;
import az.company.auth.dto.PermissionDto.*;
import az.company.auth.entity.App;
import az.company.auth.entity.Permission;
import az.company.auth.entity.RolePermission;
import az.company.auth.repository.AppRepository;
import az.company.auth.repository.PermissionRepository;
import az.company.auth.repository.RolePermissionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * A service class that implements the PermissionService interface.
 * This class is responsible for CRUD operations related to Permission entities.
 */

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper mapper;
    private final RolePermissionRepository rolePermissionRepository;
    private final AppRepository appRepository;

    @Override
    public GetPermByAppRoleIdWithTotalDto getByAppIdAndRoleIds(Long appId, Long roleId) {
        List<Permission> rootPermissions = permissionRepository.findRootPermissionsByAppId(appId);
        List<App> apps = appRepository.findAll();
        List<Permission> rolePerms = rolePermissionRepository.findByRoleId(roleId).stream()
                .map(RolePermission::getPermission)
                .collect(Collectors.toList());
        List<PermissionTotalDto> totals = new ArrayList<>();

        for(App app : apps){
            Long total = rolePermissionRepository.countByAppIdAndRoleIdAndStatus(app.getId(),roleId,'1');
            PermissionTotalDto permissionTotalDto = new PermissionTotalDto();
            permissionTotalDto.setTotal(total);
            permissionTotalDto.setKey(app.getId());
            totals.add(permissionTotalDto);
        }

        List<GetPermissionByAppRoleIdDto> result = new ArrayList<>();
        for (Permission permission : rootPermissions) {
            GetPermissionByAppRoleIdDto dto = new GetPermissionByAppRoleIdDto();
            dto.setId(permission.getId());
            dto.setAppId(appId);
            dto.setCheckable(permission.getParent()==null?false:true);
            dto.setParentId(permission.getParent()!=null?permission.getParent().getId():null);
            dto.setCheck(rolePerms.contains(permission));
            dto.setPermissionName(permission.getPermissionName());
            dto.setChildren(getChildren(permission,rolePerms,appId));
            result.add(dto);
        }

        GetPermByAppRoleIdWithTotalDto getPermByAppRoleIdWithTotalDto = new GetPermByAppRoleIdWithTotalDto();
        getPermByAppRoleIdWithTotalDto.setGetPermissionByAppRoleIdDto(result);
        getPermByAppRoleIdWithTotalDto.setPermissionTotalDtos(totals);

        return getPermByAppRoleIdWithTotalDto;
    }

    private List<GetPermissionByAppRoleIdDto> getChildren(Permission parent,List<Permission> rolePerms,Long appId) {
        List<Permission> children = permissionRepository.findChildPermissionsByParentIdAndAppId(parent.getId(),appId);
        List<GetPermissionByAppRoleIdDto> result = new ArrayList<>();
        for (Permission child : children) {
            GetPermissionByAppRoleIdDto childDto = new GetPermissionByAppRoleIdDto();
            childDto.setId(child.getId());
            childDto.setParentId(child.getParent()!=null?child.getParent().getId():null);
            childDto.setPermissionName(child.getPermissionName());
            childDto.setCheckable(child.getParent()==null?false:true);
            childDto.setAppId(appId);
            childDto.setCheck(rolePerms.contains(child));
            childDto.setChildren(getChildren(child,rolePerms,appId));
            result.add(childDto);
        }
        return result;
    }


    /**
     * Retrieves a PermissionBaseDto object with the given id.
     *
     * @param id The id of the Permission entity to retrieve.
     * @return A PermissionBaseDto object representing the retrieved entity.
     * @throws NullPointerException if no entity with the given id is found.
     */
    @Override
    public PermissionBaseDto getById(Long id) {
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new NullPointerException("not found"));
        return mapper.toDto(permission);
    }

    /**
     * Retrieves a list of all PermissionBaseDto objects.
     *
     * @return A list of PermissionBaseDto objects.
     */
    @Override
    public List<PermissionBaseDto> getAll() {
        List<Permission> entity = permissionRepository.findAll();
        return mapper.toBaseDtos(entity);
    }


    /**
     * Retrieves a list of all PermissionFullDto objects by AppId.
     * @param idDto The ID of the AppId and RoleId
     * @return A list of PermissionFullDto objects.
     */

    @Override
    public PermissionListDto getByAppIdAndRoleIds(PermissionAppRoleIdDto idDto) {

        List<Long> permissionByRoleIdIds = permissionRepository.findPermissionByRoleId(idDto.getRoleId());

        List<App> apps = appRepository.findAll();
        List<PermissionTotalDto> permissionTotalDtos = new ArrayList<>();
        PermissionListDto dto = new PermissionListDto();

        for(App app : apps){
            if(app.getId()==idDto.getAppId()){
                dto.setPermissionFullDto(permissionsByAppIdAndRoleIds(app.getId(),permissionByRoleIdIds));
                PermissionTotalDto permissionTotalDto = new PermissionTotalDto();
                permissionTotalDto.setKey(app.getId());
                permissionTotalDto.setTotal(getTotalDisabledQuantity(permissionsByAppIdAndRoleIds(app.getId(),permissionByRoleIdIds)));
                permissionTotalDtos.add(permissionTotalDto);
            }else {
                PermissionTotalDto permissionTotalDto = new PermissionTotalDto();
                permissionTotalDto.setKey(app.getId());
                permissionTotalDto.setTotal(getTotalDisabledQuantity(permissionsByAppIdAndRoleIds(app.getId(),permissionByRoleIdIds)));
                permissionTotalDtos.add(permissionTotalDto);
            }
        }

        dto.setPermissionIds(permissionByRoleIdIds.isEmpty() ? null : permissionByRoleIdIds);
        dto.setTotalsByAppId(permissionTotalDtos);

        return dto;
    }

    private List<PermissionFullDto> permissionsByAppIdAndRoleIds(Long appId,List<Long> permissionByRoleIdIds){
        List<Permission> permissionsByAppId = permissionRepository.findRootPermissionsByAppId(appId);
        List<PermissionFullDto> permissionFullDtos = mapper.toFullDtos(permissionsByAppId);

        permissionFullDtos.forEach(permission -> setDisabledRecursively(permission, permissionByRoleIdIds));
        return permissionFullDtos;
    }

    private void setDisabledRecursively(PermissionFullDto permission, List<Long> permissionByRoleIdIds){
       permission.getChildren().forEach(child -> {
           setDisabledRecursively(child, permissionByRoleIdIds);
           child.setDisabled(permissionByRoleIdIds.contains(child.getId()));
           child.setFullName(permission.getFullName() + " " + child.getFullName());

       });
    }

    private long getTotalDisabledQuantity(List<PermissionFullDto> permissions) {
        return permissions.stream()
                .mapToLong(permission -> (permission.isDisabled() ? 1L : 0L) + getTotalDisabledQuantity(permission.getChildren()))
                .sum();
    }

    /**
     * Inserts a new Permission entity.
     *
     * @param dto The PermissionBaseDto object to be inserted.
     * @return The inserted PermissionBaseDto object.
     */
    @Override
    public PermissionBaseDto insert(PermissionBaseDto dto) {
        Permission convert = mapper.toEntity(dto);
        permissionRepository.save(convert);
        return dto;
    }

    /**
     * Deletes the Permission entity with the given id.
     *
     * @param id The id of the entity to be deleted.
     * @throws NullPointerException if no entity with the given id is found.
     */
    @Override
    public void deletePermission(Long id) {
        Permission entity = permissionRepository.findById(id).orElseThrow(() -> new NullPointerException("ID not found"));
        permissionRepository.delete(entity);
    }


}
