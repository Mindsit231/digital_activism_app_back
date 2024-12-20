package mindsit.digitalactivismapp.model.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mindsit.digitalactivismapp.config.Role;
import mindsit.digitalactivismapp.model.MyEntity;
import mindsit.digitalactivismapp.model.tag.MemberTag;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
@ToString
public class Member implements MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "email_verified", nullable = false)
    @ColumnDefault("false")
    private boolean emailVerified;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @ColumnDefault("CURRENT_DATE")
    private Date creationDate;

    @Column(name = "pfp_name")
    private String pfpName;

    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    @OneToMany(
            fetch = FetchType.EAGER,
            targetEntity = MemberTag.class
    )
    @JoinColumn(name = "member_id", updatable = false, insertable = false)
    private List<MemberTag> memberTags;

    public Member(String username, String email) {
        this.username = username;
        this.email = email;
        this.password = "";
        this.role = Role.AUTHENTICATED;
        this.creationDate = new Date();
    }
}
