package az.company.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_permission", schema = "security")
public class UserPermission extends Base<Long> {


    @Column(name = "access")
    private boolean access;

    @Column(name = "status", length = 1)
    private Character status = '1';

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

    public UserPermission(User user, Permission permission) {
        this.user = user;
        this.permission = permission;
    }

}
