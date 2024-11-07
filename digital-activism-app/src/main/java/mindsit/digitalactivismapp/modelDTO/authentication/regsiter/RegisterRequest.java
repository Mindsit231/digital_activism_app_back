package mindsit.digitalactivismapp.modelDTO.authentication.regsiter;

public record RegisterRequest(
        String username,
        String email,
        String password
) {
}
