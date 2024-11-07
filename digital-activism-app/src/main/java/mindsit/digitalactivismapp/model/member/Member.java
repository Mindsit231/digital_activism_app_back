package mindsit.digitalactivismapp.model.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import mindsit.digitalactivismapp.config.Role;
import mindsit.digitalactivismapp.model.MyEntity;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "member")
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

    public Member(String username, String email) {
        this.username = username;
        this.email = email;
        this.password = "";
        this.role = Role.AUTHENTICATED;
        this.creationDate = new Date();
    }
}
