package mindsit.digitalactivismapp.model.message;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mindsit.digitalactivismapp.model.MyEntity;

@Getter
@Setter
@Entity
@Table(name = "message_video")
public class MessageVideo implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "message_id", nullable = false)
    private Long messageId;
}
