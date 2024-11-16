package mindsit.digitalactivismapp.model.tag;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tag")
public class Tag implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
