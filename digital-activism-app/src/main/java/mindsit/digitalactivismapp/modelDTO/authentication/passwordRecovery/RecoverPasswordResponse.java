package mindsit.digitalactivismapp.modelDTO.authentication.passwordRecovery;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RecoverPasswordResponse {
    List<String> errors = new ArrayList<>();
    Boolean success = false;
}
