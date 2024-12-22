package mindsit.digitalactivismapp.mapper;

import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.member.MemberDTO;
import mindsit.digitalactivismapp.modelDTO.authentication.register.RegisterRequest;
import mindsit.digitalactivismapp.modelDTO.member.MemberDTOShort;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class MemberMapper {
    public abstract MemberDTO memberToMemberDTO(Member member);

    public abstract MemberDTOShort memberToMemberDTOShort(Member member);

    @Mapping(target = "role", expression = "java(Role.AUTHENTICATED)")
    @Mapping(target = "creationDate", expression = "java(new Date())")
    public abstract Member memberToRegisterMapper(RegisterRequest registerRequest);
}
