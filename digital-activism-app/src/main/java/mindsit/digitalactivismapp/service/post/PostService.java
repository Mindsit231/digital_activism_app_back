package mindsit.digitalactivismapp.service.post;

import mindsit.digitalactivismapp.model.post.Post;
import mindsit.digitalactivismapp.repository.post.PostRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService extends EntityService<Post, PostRepository> {
    public PostService(PostRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
