package mindsit.digitalactivismapp.model.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;

@Getter
@Setter
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
}
