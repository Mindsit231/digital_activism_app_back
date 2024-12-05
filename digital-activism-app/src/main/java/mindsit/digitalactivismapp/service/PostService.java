package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.mapper.PostMapper;
import mindsit.digitalactivismapp.model.post.Post;
import mindsit.digitalactivismapp.model.post.PostImage;
import mindsit.digitalactivismapp.model.post.PostVideo;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.modelDTO.post.PostDTO;
import mindsit.digitalactivismapp.modelDTO.post.PostImageDTO;
import mindsit.digitalactivismapp.modelDTO.post.PostVideoDTO;
import mindsit.digitalactivismapp.repository.post.PostImageRepository;
import mindsit.digitalactivismapp.repository.post.PostRepository;
import mindsit.digitalactivismapp.repository.post.PostVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService extends EntityService<Post, PostRepository> {

    private final PostMapper postMapper;

    @Autowired
    public PostService(PostRepository repository,
                       PostMapper postMapper) {
        super(repository);
        this.postMapper = postMapper;
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }

    public List<Post> fetchPostsLimitedByCommunityId(FetchEntityLimited fetchEntityLimited) {
        return entityRepository.fetchPostsLimitedByCommunityId(fetchEntityLimited.limit(), fetchEntityLimited.offset(), fetchEntityLimited.optionalId());
    }

    public List<PostDTO> fetchPostDTOSLimitedByCommunityId(FetchEntityLimited fetchEntityLimited) {
        return postMapper.postToPostDTO(fetchPostsLimitedByCommunityId(fetchEntityLimited));
    }
}
