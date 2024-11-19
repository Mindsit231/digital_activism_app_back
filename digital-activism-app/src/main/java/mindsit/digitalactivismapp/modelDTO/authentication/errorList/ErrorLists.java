package mindsit.digitalactivismapp.modelDTO.authentication.errorList;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorLists {
    private List<ErrorList> errorLists = new ArrayList<>();

    public ErrorList findErrorListByName(String name) {
        for (ErrorList errorList : errorLists) {
            if (errorList.getName().equals(name)) {
                return errorList;
            }
        }
        return null;
    }

    public void add(ErrorList errorList) {
        errorLists.add(errorList);
    }

    public boolean hasNoErrors() {
        boolean hasNoErrors = true;

        for (ErrorList errorList : errorLists) {
            if (errorList.hasErrors()) {
                hasNoErrors = false;
                break;
            }
        }

        return hasNoErrors;
    }
}
