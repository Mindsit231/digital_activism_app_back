package mindsit.digitalactivismapp.service.member;

import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.MemberDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MemberDTOMapper implements Function<Member, MemberDTO> {
    @Override
    public MemberDTO apply(Member member) {
        return new MemberDTO(
                member.getId(),
                member.getUsername(),
                member.getEmail(),
                member.getRole(),
                member.getCreationDate(),
                member.getPfpName(),
                member.getToken()
        );
    }
}
