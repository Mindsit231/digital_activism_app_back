package mindsit.digitalactivismapp.model.query.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordByEmail {
    private String email;
    private String newPassword;
}
