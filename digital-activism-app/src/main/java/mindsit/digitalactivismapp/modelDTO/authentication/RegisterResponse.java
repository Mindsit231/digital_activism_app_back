package mindsit.digitalactivismapp.modelDTO.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mindsit.digitalactivismapp.modelDTO.MemberDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private MemberDTO memberDTO;
    private List<ErrorList> errorLists = new ArrayList<>();
}
