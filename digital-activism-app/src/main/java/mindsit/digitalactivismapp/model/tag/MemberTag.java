package mindsit.digitalactivismapp.model.tag;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;
@Getter
@Setter
@Entity
@Table(name = "member_tag")
public class MemberTag implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "tag_id", nullable = false)
    private Long tagId;
}
