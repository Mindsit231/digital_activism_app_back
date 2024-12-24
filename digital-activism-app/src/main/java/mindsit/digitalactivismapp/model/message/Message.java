package mindsit.digitalactivismapp.model.message;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;
import mindsit.digitalactivismapp.model.member.Member;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date timestamp;

    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Member.class
    )
    @JoinColumn(name = "member_id", updatable = false, insertable = false, nullable = false)
    private Member member;

    public Message(String text, Long campaignId, Long memberId) {
        this.text = text;
        this.campaignId = campaignId;
        this.memberId = memberId;
    }
}
