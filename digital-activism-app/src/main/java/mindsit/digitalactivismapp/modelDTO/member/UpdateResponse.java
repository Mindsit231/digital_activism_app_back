package mindsit.digitalactivismapp.modelDTO.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mindsit.digitalactivismapp.modelDTO.MemberDTO;
import mindsit.digitalactivismapp.modelDTO.authentication.errorList.ErrorLists;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResponse {
    private MemberDTO memberDTO;
    private ErrorLists errorLists = new ErrorLists();
}
