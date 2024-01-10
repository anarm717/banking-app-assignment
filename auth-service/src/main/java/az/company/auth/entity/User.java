package az.company.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", schema = "security")
public class User extends Base<Long> {

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$",
            message = "Password must have at least 1 uppercase letter, 1 number, 1 symbol, and be at least 6 characters long")
    @Column(length = 512)
    private String password;


    @Column(name = "first_name", length = 100, nullable = false)
    private String name;

    @Column(name = "last_name", length = 150)
    private String surname;

    @Column(name = "father_name", length = 100)
    private String fatherName;

    @Column(name = "mobile")
//    @Pattern(regexp = "^\\(05\\d{2}\\)\\d{7}$", message = "Invalid mobile number format")
    private String mobile;

    @Email
    private String email;

    @Column(name = "note")
    private String note;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "status", length = 1)
    private Character status = '1';

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @OneToMany(mappedBy = "user", cascade = { CascadeType.MERGE, CascadeType.PERSIST})
    @Where(clause = "status = '1'")
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "user", cascade = { CascadeType.MERGE, CascadeType.PERSIST})
    private List<UserPermission> userPermissions = new ArrayList<>();

    public User(Long id) {
        this.setId(id);
    }


}
