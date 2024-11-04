package mindsit.digitalactivismapp.service.tag;

import mindsit.digitalactivismapp.model.tag.Tag;
import mindsit.digitalactivismapp.repository.tag.TagRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService extends EntityService<Tag, TagRepository> {
    public TagService(TagRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
