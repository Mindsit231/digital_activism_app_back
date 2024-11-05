package mindsit.digitalactivismapp.custom;

import java.util.Optional;

public class Functions {

    public static Optional<String> getToken(String authHeader) {
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        return Optional.ofNullable(token);
    }

    public static Integer generateRandomNumber(int length) {
        return (int) (Math.random() * Math.pow(10, length));
    }
}
