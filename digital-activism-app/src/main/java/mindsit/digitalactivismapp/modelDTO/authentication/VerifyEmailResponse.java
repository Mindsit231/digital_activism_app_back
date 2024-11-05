package mindsit.digitalactivismapp.modelDTO.authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VerifyEmailResponse {
    private boolean success = false;
    private List<String> errors = new ArrayList<>();
}
