package mindsit.digitalactivismapp.modelDTO.authentication.errorList;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorLists {
    private List<ErrorList> errorList = new ArrayList<>();

    public ErrorList findErrorListByName(String name) {
        for (ErrorList errorList : errorList) {
            if (errorList.getName().equals(name)) {
                return errorList;
            }
        }
        return null;
    }

    public void add(ErrorList errorList) {
        if(findErrorListByName(errorList.getName()) == null) {
            this.errorList.add(errorList);
        }
    }

    public boolean hasNoErrors() {
        boolean hasNoErrors = true;

        for (ErrorList errorList : errorList) {
            if (errorList.hasErrors()) {
                hasNoErrors = false;
                break;
            }
        }

        return hasNoErrors;
    }
}
