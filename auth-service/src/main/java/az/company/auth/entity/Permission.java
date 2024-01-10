package az.company.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "permissions", schema = "security")
@NamedEntityGraph(
        name ="Permission.children",
        attributeNodes = @NamedAttributeNode("children")
)
public class Permission extends Base<Long> implements GrantedAuthority {

    @Column(name = "perm_name", length = 100)
    private String permissionName;

    @Column(name = "perm_desc")
    private String description;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private App appId;

    @OneToMany(mappedBy = "permission")
    private List<RolePermission> rolePermissions;

    @OneToMany(mappedBy = "permission")
    private List<UserPermission> userPermissions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Permission parent;

    @OneToMany(mappedBy ="parent")
    private List<Permission> children = new ArrayList<>();

    public Permission(Long permissionId) {
        this.setId(permissionId);
    }

    @Override
    public String getAuthority() {
        return permissionName;
    }

}
