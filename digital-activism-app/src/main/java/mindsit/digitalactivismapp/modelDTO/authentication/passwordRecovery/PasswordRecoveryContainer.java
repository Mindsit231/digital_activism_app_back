package mindsit.digitalactivismapp.modelDTO.authentication.passwordRecovery;

import lombok.Getter;
import lombok.Setter;
import mindsit.digitalactivismapp.model.misc.EmailContainer;

@Getter
@Setter
public class PasswordRecoveryContainer extends EmailContainer {
    private String token;
    private String pagePath;

    public PasswordRecoveryContainer(String to, String subject, String body, boolean isHTML, String template, String token, String pagePath) {
        super(to, subject, body, isHTML, template);
        this.token = token;
        this.pagePath = pagePath;
    }
}
