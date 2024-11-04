package mindsit.digitalactivismapp.model.tag;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;

@Getter
@Setter
@Entity
@Table(name = "tag")
public class Tag implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
