package mindsit.digitalactivismapp.model.message;

import lombok.Getter;

@Getter
public enum Status {
    SENDING("SENDING"),
    SENT("SENT"),
    RECEIVED("RECEIVED"),
    READ("READ");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
