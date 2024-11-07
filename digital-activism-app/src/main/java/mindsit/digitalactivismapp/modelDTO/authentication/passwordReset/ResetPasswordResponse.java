package mindsit.digitalactivismapp.modelDTO.authentication.passwordReset;

import lombok.Getter;
import lombok.Setter;
import mindsit.digitalactivismapp.modelDTO.authentication.errorList.ErrorList;
import mindsit.digitalactivismapp.modelDTO.authentication.errorList.ErrorListsImpl;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResetPasswordResponse implements ErrorListsImpl {
    private List<ErrorList> errorLists = new ArrayList<>();
    private Boolean success = false;
}
