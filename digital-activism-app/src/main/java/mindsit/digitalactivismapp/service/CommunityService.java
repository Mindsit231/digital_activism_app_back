package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.mapper.community.CommunityDTOSMapper;
import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.modelDTO.CommunityDTO;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.repository.community.CommunityRepository;
import mindsit.digitalactivismapp.service.misc.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CommunityService extends EntityService<Community, CommunityRepository> {
    protected final CommunityDTOSMapper communityDTOSMapper;
    protected final FileService fileService = new FileService();

    public CommunityService(CommunityRepository repository,
                            CommunityDTOSMapper communityDTOSMapper) {
        super(repository);
        this.communityDTOSMapper = communityDTOSMapper;
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
