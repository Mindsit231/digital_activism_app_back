package mindsit.digitalactivismapp.mapper.community;

import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.modelDTO.CommunityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class CommunityDTOSMapper implements Function<List<Community>, List<CommunityDTO>> {
    protected final CommunityDTOMapper communityDTOMapper;

    @Autowired
    public CommunityDTOSMapper(CommunityDTOMapper communityDTOMapper) {
        this.communityDTOMapper = communityDTOMapper;
    }

    @Override
    public List<CommunityDTO> apply(List<Community> communities) {
        return communities.stream().map(communityDTOMapper).toList();
    }
}
