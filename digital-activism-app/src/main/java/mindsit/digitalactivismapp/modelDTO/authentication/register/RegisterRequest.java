package mindsit.digitalactivismapp.modelDTO.authentication.register;

public record RegisterRequest(
        String username,
        String email,
        String password
) {
}
