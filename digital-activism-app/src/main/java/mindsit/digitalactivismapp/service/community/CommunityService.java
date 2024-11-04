package mindsit.digitalactivismapp.service.community;

import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.repository.community.CommunityRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommunityService extends EntityService<Community, CommunityRepository> {
    public CommunityService(CommunityRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
