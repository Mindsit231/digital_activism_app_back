package mindsit.digitalactivismapp.model.campaign;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;

@Getter
@Setter
@Entity
@Table(name = "member_campaign")
@AllArgsConstructor
@NoArgsConstructor
public class MemberCampaign implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    public MemberCampaign(Long campaignId, Long memberId) {
        this.memberId = memberId;
        this.campaignId = campaignId;
    }
}
