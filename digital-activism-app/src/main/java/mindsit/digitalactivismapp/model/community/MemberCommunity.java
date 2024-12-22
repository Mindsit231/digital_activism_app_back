package mindsit.digitalactivismapp.model.community;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "member_community")
@AllArgsConstructor
@NoArgsConstructor
public class MemberCommunity implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "community_id", nullable = false)
    private Long communityId;

    @Column(name = "is_admin")
    @ColumnDefault("false")
    private Boolean isAdmin;

    public MemberCommunity(Long communityId, Long memberId) {
        this.memberId = memberId;
        this.communityId = communityId;
    }
}
