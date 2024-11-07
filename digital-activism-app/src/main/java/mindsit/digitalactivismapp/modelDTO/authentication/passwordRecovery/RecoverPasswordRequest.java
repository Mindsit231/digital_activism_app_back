package mindsit.digitalactivismapp.modelDTO.authentication.passwordRecovery;

public record RecoverPasswordRequest(
        String email,
        String pagePath
) {

}
