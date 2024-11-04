package mindsit.digitalactivismapp.model.community;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;

@Getter
@Setter
@Entity
@Table(name = "member_community")
public class MemberCommunity implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "community_id", nullable = false)
    private Long communityId;
}
