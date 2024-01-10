package az.company.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "object_type")
@NamedEntityGraph(
        name = "ObjectType.objectEntities",
        attributeNodes = @NamedAttributeNode("objects")
)
public class ObjectType extends Base<Long>{

    @Column(name = "type_name", nullable = false, length = 50)
    private String typeName;

    @Column(name = "desc", length = 300)
    private String description;

    @Column(name = "visibility", nullable = false)
    private boolean visibility;

    @OneToMany(mappedBy = "objectType", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ObjectEntity> objects;

}
