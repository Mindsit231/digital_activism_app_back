package mindsit.digitalactivismapp.mapper.community;

import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.modelDTO.CommunityDTO;

import java.util.function.Function;

public class CommunityDTOMapper implements Function<Community, CommunityDTO> {
    @Override
    public CommunityDTO apply(Community community) {
        return new CommunityDTO(
                community.getName(),
                community.getDescription(),
                community.getLogoName(),
                community.getBannerName(),
                community.getTimestamp()
        );
    }
}
