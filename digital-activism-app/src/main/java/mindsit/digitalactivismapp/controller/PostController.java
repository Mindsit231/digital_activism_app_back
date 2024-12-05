package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.post.Post;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.modelDTO.post.PostDTO;
import mindsit.digitalactivismapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostController extends EntityController<Post, PostService> {

    @Autowired
    public PostController(PostService postService) {
        super(postService, Post.class);
    }

    @PostMapping("/authenticated/post/fetch-posts-limited-by-community-id")
    public ResponseEntity<List<PostDTO>> fetchPostDTOSLimitedByCommunityId(@RequestBody FetchEntityLimited fetchEntityLimited) {
        return ResponseEntity.ok(entityService.fetchPostDTOSLimitedByCommunityId(fetchEntityLimited));
    }
}
