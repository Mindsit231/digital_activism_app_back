package mindsit.digitalactivismapp.modelDTO.reCaptcha;

public record ReCaptchaRequest(
        String secret,
        String response,
        String remoteIp
) {
}
