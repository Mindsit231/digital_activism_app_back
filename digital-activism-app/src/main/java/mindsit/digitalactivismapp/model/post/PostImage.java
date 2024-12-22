package mindsit.digitalactivismapp.model.post;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;

@Data
@NoArgsConstructor
@Entity
@Table(name = "post_image")
public class PostImage implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    public PostImage(String name) {
        this.name = name;
    }
}
