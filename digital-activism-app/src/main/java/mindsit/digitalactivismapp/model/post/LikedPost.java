package mindsit.digitalactivismapp.model.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "liked_post")
public class LikedPost implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date timestamp;
}
