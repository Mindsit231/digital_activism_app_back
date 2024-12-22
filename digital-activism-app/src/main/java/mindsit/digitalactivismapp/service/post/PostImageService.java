package mindsit.digitalactivismapp.service.post;

import mindsit.digitalactivismapp.model.post.PostImage;
import mindsit.digitalactivismapp.repository.post.PostImageRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostImageService extends EntityService<PostImage, PostImageRepository> {

    @Autowired
    public PostImageService(PostImageRepository repository) {
        super(repository);
    }

    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
