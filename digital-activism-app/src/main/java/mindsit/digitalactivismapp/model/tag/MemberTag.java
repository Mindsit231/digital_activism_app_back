package mindsit.digitalactivismapp.model.tag;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;
import mindsit.digitalactivismapp.model.member.Member;

@Data
@NoArgsConstructor
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

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Tag.class
    )
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private Tag tag;

    public MemberTag(Long memberId, Long tagId) {
        this.memberId = memberId;
        this.tagId = tagId;
    }
}
