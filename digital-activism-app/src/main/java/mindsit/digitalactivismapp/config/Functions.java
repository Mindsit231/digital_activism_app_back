package mindsit.digitalactivismapp.config;

import java.util.Optional;

public class Functions {

    public static Optional<String> getToken(String authHeader) {
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        return Optional.ofNullable(token);
    }
}
