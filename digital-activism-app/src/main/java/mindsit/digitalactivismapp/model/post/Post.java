package mindsit.digitalactivismapp.model.post;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mindsit.digitalactivismapp.model.MyEntity;

@Data
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "visibility", nullable = false)
    private Visibility visibility;

    @Column(name = "community_id", nullable = false)
    private Long communityId;
}
