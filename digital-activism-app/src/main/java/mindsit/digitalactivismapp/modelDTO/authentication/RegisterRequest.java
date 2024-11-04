package mindsit.digitalactivismapp.modelDTO.authentication;

public record RegisterRequest(
        String username,
        String email,
        String password
) {
}
