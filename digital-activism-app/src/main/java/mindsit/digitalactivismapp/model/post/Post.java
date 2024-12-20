package mindsit.digitalactivismapp.model.post;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mindsit.digitalactivismapp.model.MyEntity;
import mindsit.digitalactivismapp.model.tag.MemberTag;
import mindsit.digitalactivismapp.model.tag.PostTag;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault("CURRENT_DATE")
    private Date timestamp;

    @Column(name = "community_id", nullable = false)
    private Long communityId;

    @OneToMany(
            fetch = FetchType.EAGER,
            targetEntity = PostTag.class
    )
    @JoinColumn(name = "post_id", updatable = false, insertable = false)
    private List<MemberTag> postTags;

    @OneToMany(
            fetch = FetchType.EAGER,
            targetEntity = PostImage.class
    )
    @JoinColumn(name = "post_id", updatable = false, insertable = false)
    private List<PostImage> postImages;

    @OneToMany(
            fetch = FetchType.EAGER,
            targetEntity = PostVideo.class
    )
    @JoinColumn(name = "post_id", updatable = false, insertable = false)
    private List<PostVideo> postVideos;
}
