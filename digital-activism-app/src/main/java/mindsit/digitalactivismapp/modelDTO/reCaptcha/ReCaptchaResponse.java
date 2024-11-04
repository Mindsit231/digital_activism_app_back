package mindsit.digitalactivismapp.modelDTO.reCaptcha;

import java.util.List;

public record ReCaptchaResponse (
        boolean success,
        String challenge_ts,
        String hostname,
        List<String> errorCodes
) {
}
