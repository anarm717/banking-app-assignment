package az.company.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "object")
@NamedEntityGraph(
        name = "ObjectEntity.objectType",
        attributeNodes = @NamedAttributeNode("objectType")
)
public class ObjectEntity extends Base<Long>{

    @Column(name = "parent_id")
    private Long parentId;

//    @Column(name = "permission_id")
    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_type_id", referencedColumnName = "id", nullable = false)
    private ObjectType objectType;

    @Column(name = "object_id_name", length = 100)
    private String objectIdName;

    @Column(name = "caption", length = 200)
    private String caption;

    @Column(name = "property", columnDefinition = "jsonb")
    private String property;

    @Column(name = "desc", length = 300)
    private String description;

    @Column(name = "required",nullable = true)
    private boolean required;

}
