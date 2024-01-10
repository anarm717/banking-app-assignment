package az.company.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles", schema = "security")
public class Role extends Base<Long> implements GrantedAuthority {

    @Column(length = 100)
    private String roleName;

    @Column
    private String roleDesc;

    @Column(name = "status", length = 1)
    private Character status = '1';

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @OneToMany(mappedBy = "role")
    @Where(clause = "status = '1'")
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "roleId", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<RolePermission> rolePermissions ;

    public Role(Long roleId) {
        this.setId(roleId);
    }

//    @JsonIgnore
//    @ManyToMany
//    @JoinTable(
//            name = "role_permission",
//            schema = "security",
//            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")}
//    )
//    private Set<Permission> permissions;
//
//    @ManyToMany(mappedBy = "roles")
//    private Set<Method> methods;
//
//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users;
//
//
//    public Role(Integer i) {
//        setId(i);
//    }
//
    @Override
    public String getAuthority() {
        return roleName;
    }
}
