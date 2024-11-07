package mindsit.digitalactivismapp.modelDTO.authentication.verifyEmail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SendEmailVerificationResponse {
    private List<String> errors = new ArrayList<>();
}
