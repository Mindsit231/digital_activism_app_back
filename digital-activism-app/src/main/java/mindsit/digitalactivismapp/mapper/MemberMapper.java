package mindsit.digitalactivismapp.mapper;

import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.authentication.register.RegisterRequest;
import mindsit.digitalactivismapp.modelDTO.member.MemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MemberMapper {
    public abstract MemberDTO memberToMemberDTO(Member member);

    @Mapping(target = "role", expression = "java(Role.AUTHENTICATED)")
    @Mapping(target = "creationDate", expression = "java(new Date())")
    public abstract Member memberToRegisterMapper(RegisterRequest registerRequest);

    public List<MemberDTO> memberToMemberDTO(List<Member> members) {
        return members.stream().map(this::memberToMemberDTO).toList();
    }

    @Mapping(target = "id", source = "member.id")
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "token", ignore = true)
    public abstract MemberDTO memberToMemberDTOShort(Member member);

    public List<MemberDTO> memberToMemberDTOShort(List<Member> members) {
        return members.stream().map(this::memberToMemberDTOShort).toList();
    }
}
