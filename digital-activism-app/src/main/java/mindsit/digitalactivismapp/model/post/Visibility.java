package mindsit.digitalactivismapp.model.post;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Visibility {
    PUBLIC("PUBLIC"),
    PRIVATE("PRIVATE");

    private final String visibility;

    Visibility(String visibility) {
        this.visibility = visibility;
    }

    public static Visibility find(String visibility) {
        return Arrays.stream(values())
                .filter(v -> v.visibility.equals(visibility))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Visibility not found"));
    }
}
