package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.mapper.community.CommunityDTOSMapper;
import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.modelDTO.CommunityDTO;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.repository.community.CommunityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommunityService extends EntityService<Community, CommunityRepository> {
    protected final CommunityDTOSMapper communityDTOSMapper = new CommunityDTOSMapper();

    public CommunityService(CommunityRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }

    public Integer getTableLength() {
        return entityRepository.getTableLength();
    }

    public List<CommunityDTO> fetchCommunitiesLimited(FetchEntityLimited fetchEntityLimited) {
        return communityDTOSMapper.apply(entityRepository.fetchCommunitiesLimited(fetchEntityLimited.limit(), fetchEntityLimited.offset()));
    }
}
