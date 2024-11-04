package mindsit.digitalactivismapp.model.message;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Getter
@Setter
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

    @Column(name = "status")
    private Status status;

    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;
}
