package mindsit.digitalactivismapp.service.tag;

import mindsit.digitalactivismapp.model.tag.PostTag;
import mindsit.digitalactivismapp.repository.tag.PostTagRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostTagService extends EntityService<PostTag, PostTagRepository> {
    public PostTagService(PostTagRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
