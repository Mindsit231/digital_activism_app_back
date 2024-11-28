package mindsit.digitalactivismapp.mapper.community;

import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.modelDTO.CommunityDTO;

import java.util.List;
import java.util.function.Function;

public class CommunityDTOSMapper implements Function<List<Community>, List<CommunityDTO>> {
    protected final CommunityDTOMapper communityDTOMapper = new CommunityDTOMapper();

    @Override
    public List<CommunityDTO> apply(List<Community> communities) {
        return communities.stream().map(communityDTOMapper).toList();
    }
}
