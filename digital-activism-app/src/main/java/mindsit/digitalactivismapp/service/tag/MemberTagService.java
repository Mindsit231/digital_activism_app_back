package mindsit.digitalactivismapp.service.tag;

import mindsit.digitalactivismapp.model.tag.MemberTag;
import mindsit.digitalactivismapp.repository.tag.MemberTagRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberTagService extends EntityService<MemberTag, MemberTagRepository> {
    public MemberTagService(MemberTagRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
