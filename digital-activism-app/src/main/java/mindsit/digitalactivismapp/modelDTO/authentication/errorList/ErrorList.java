package mindsit.digitalactivismapp.modelDTO.authentication.errorList;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorList {
    private String name;
    private List<String> errors = new ArrayList<>();

    public ErrorList(String name) {
        this.name = name;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
