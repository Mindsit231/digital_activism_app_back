package mindsit.digitalactivismapp.service.community;

import mindsit.digitalactivismapp.model.community.MemberCommunity;
import mindsit.digitalactivismapp.repository.community.MemberCommunityRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberCommunityService extends EntityService<MemberCommunity, MemberCommunityRepository> {
    public MemberCommunityService(MemberCommunityRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
