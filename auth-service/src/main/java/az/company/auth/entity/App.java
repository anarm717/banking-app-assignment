package az.company.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Entity
@Getter
@Setter
public class App extends Base<Long> {
    @Column(name = "app_name", length = 100)
    private String appName;

    @Column(name = "app_desc")
    private String appDesc;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @OneToMany(mappedBy = "appId")
    private List<Permission> permissions = new ArrayList<>();
}
