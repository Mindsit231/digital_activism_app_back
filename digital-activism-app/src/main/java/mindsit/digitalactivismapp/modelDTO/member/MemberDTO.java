package mindsit.digitalactivismapp.modelDTO.member;

import mindsit.digitalactivismapp.config.Role;

import java.util.Date;

public record MemberDTO(
        Long id,
        String username,
        String email,
        Boolean emailVerified,
        Role role,
        Date creationDate,
        String pfpName,
        String token
) {
}
