package mindsit.digitalactivismapp.model.campaign;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mindsit.digitalactivismapp.model.MyEntity;
import mindsit.digitalactivismapp.model.member.Member;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "campaign")
public class Campaign implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date creationDate;

    @Column(name = "community_id", nullable = false)
    private Long communityId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Member.class
    )
    @JoinColumn(name = "member_id", updatable = false, insertable = false, nullable = false)
    private Member member;

    @ManyToMany(
            fetch = FetchType.EAGER,
            targetEntity = Member.class
    )
    @JoinTable(
            name = "member_campaign",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> members;
}
