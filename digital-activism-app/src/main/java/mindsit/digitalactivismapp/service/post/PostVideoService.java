package mindsit.digitalactivismapp.service.post;

import mindsit.digitalactivismapp.model.post.PostVideo;
import mindsit.digitalactivismapp.repository.post.PostVideoRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.stereotype.Service;

@Service
public class PostVideoService extends EntityService<PostVideo, PostVideoRepository> {

    public PostVideoService(PostVideoRepository repository) {
        super(repository);
    }

    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
