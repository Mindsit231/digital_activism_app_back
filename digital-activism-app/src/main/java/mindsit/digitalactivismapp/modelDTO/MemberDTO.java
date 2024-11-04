package mindsit.digitalactivismapp.modelDTO;

import mindsit.digitalactivismapp.config.Role;

import java.util.Date;

public record MemberDTO(
        Long id,
        String username,
        String email,
        Role role,
        Date creationDate,
        String pfpName,
        String token
) {
}
