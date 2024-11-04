package mindsit.digitalactivismapp.model.post;

import lombok.Getter;

@Getter
public enum Visibility {
    PUBLIC("PUBLIC"),
    PRIVATE("PRIVATE");

    private final String visibility;

    Visibility(String visibility) {
        this.visibility = visibility;
    }
}
