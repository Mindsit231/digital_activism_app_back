package mindsit.digitalactivismapp.modelDTO.authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmailVerificationResponse {
    private String verificationCodeHash;
    private List<String> errors = new ArrayList<>();
}
