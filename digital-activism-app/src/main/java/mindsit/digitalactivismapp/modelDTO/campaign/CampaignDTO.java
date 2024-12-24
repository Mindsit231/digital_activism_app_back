package mindsit.digitalactivismapp.modelDTO.campaign;

import mindsit.digitalactivismapp.modelDTO.member.MemberDTO;

import java.util.Date;

public record CampaignDTO(
        Long id,
        String name,
        String description,
        Date startDate,
        Date endDate,
        Date creationDate,
        Long communityId,
        MemberDTO memberDTO,
        Boolean participating
) {
}
