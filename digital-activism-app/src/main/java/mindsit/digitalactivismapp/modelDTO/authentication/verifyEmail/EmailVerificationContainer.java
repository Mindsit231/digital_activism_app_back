package mindsit.digitalactivismapp.modelDTO.authentication.verifyEmail;

import lombok.*;
import mindsit.digitalactivismapp.model.misc.EmailContainer;

@Getter
@Setter
public class EmailVerificationContainer extends EmailContainer {
    private int verificationCode;

    public EmailVerificationContainer(String to, String subject, String body, boolean isHTML, String template, int verificationCode) {
        super(to, subject, body, isHTML, template);
        this.verificationCode = verificationCode;
    }
}
