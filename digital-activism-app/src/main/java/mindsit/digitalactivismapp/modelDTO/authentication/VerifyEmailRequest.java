package mindsit.digitalactivismapp.modelDTO.authentication;

public record VerifyEmailRequest(
        String email,
        String verificationCode,
        String verificationCodeHash) {

}
