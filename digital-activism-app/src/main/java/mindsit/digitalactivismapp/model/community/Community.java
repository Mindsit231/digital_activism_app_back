package mindsit.digitalactivismapp.model.community;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mindsit.digitalactivismapp.model.MyEntity;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "community")
public class Community implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "logo_name")
    private String logoName;

    @Column(name = "banner_name")
    private String bannerName;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date timestamp;
}
