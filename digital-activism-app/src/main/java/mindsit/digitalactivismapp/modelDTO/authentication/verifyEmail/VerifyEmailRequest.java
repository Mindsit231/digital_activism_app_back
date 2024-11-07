package mindsit.digitalactivismapp.modelDTO.authentication.verifyEmail;

public record VerifyEmailRequest(
        String email,
        String verificationCode) {

}
