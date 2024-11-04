package mindsit.digitalactivismapp.service.post;

import mindsit.digitalactivismapp.model.post.PostImage;
import mindsit.digitalactivismapp.repository.post.PostImageRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostImageService extends EntityService<PostImage, PostImageRepository> {
    public PostImageService(PostImageRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
