package mindsit.digitalactivismapp.modelDTO.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mindsit.digitalactivismapp.modelDTO.MemberDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTOOptional {
    private MemberDTO memberDTO;
    private List<String> errors;
}
