package mindsit.digitalactivismapp.controller.post;

import mindsit.digitalactivismapp.controller.EntityController;
import mindsit.digitalactivismapp.model.post.Post;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.modelDTO.post.PostDTO;
import mindsit.digitalactivismapp.modelDTO.post.PostRequest;
import mindsit.digitalactivismapp.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static mindsit.digitalactivismapp.controller.AuthenticationController.AUTHORIZATION_HEADER;

@RestController
public class PostController extends EntityController<Post, PostService> {

    @Autowired
    public PostController(PostService postService) {
        super(postService, Post.class);
    }

    @PostMapping("/authenticated/community-admin/post/add")
    public ResponseEntity<PostDTO> addPost(@RequestBody PostRequest postRequest, @RequestHeader(AUTHORIZATION_HEADER) String authHeader) {
        return entityService.addPost(postRequest, authHeader);
    }

    @GetMapping("/authenticated/post/get-table-length-by-community-id")
    public ResponseEntity<Integer> getTableLengthByCommunityId(@RequestParam Long communityId) {
        return ResponseEntity.ok(entityService.getTableLengthByCommunityId(communityId));
    }

    @PostMapping("/authenticated/post/fetch-limited-by-community-id")
    public ResponseEntity<List<PostDTO>> fetchPostDTOSLimitedByCommunityId(@RequestBody FetchEntityLimited fetchEntityLimited,
                                                                           @RequestHeader(AUTHORIZATION_HEADER) String authHeader) {
        return ResponseEntity.ok(entityService.fetchPostDTOSLimitedByCommunityId(fetchEntityLimited, authHeader));
    }

    @GetMapping("/authenticated/post/toggle-like")
    public ResponseEntity<Boolean> toggleLike(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                                              @RequestParam Long postId) {
        return ResponseEntity.ok(entityService.toggleLike(postId, authHeader));
    }
}
