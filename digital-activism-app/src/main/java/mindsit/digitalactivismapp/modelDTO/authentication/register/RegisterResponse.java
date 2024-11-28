package mindsit.digitalactivismapp.modelDTO.authentication.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mindsit.digitalactivismapp.modelDTO.authentication.errorList.ErrorList;
import mindsit.digitalactivismapp.modelDTO.authentication.errorList.ErrorLists;
import mindsit.digitalactivismapp.modelDTO.authentication.errorList.ErrorListsImpl;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse implements ErrorListsImpl {
    private String token;
    private ErrorLists errorLists = new ErrorLists();

    public boolean hasNoErrors() {
        boolean hasNoErrors = true;

        for (ErrorList errorList : errorLists.getErrorList()) {
            if (!errorList.getErrors().isEmpty()) {
                hasNoErrors = false;
                break;
            }
        }

        return hasNoErrors;
    }
}
