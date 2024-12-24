package mindsit.digitalactivismapp.modelDTO.message;

import mindsit.digitalactivismapp.modelDTO.member.MemberDTO;

import java.util.Date;

public record MessageDTO(
        Long id,
        String text,
        Date timestamp,
        Long campaignId,
        Long memberId,
        MemberDTO memberDTO
) {

}
