package mindsit.digitalactivismapp.service.post;

import mindsit.digitalactivismapp.model.post.LikedPost;
import mindsit.digitalactivismapp.repository.post.LikedPostRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikedPostService extends EntityService<LikedPost, LikedPostRepository> {
    public LikedPostService(LikedPostRepository entityRepository) {
        super(entityRepository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
