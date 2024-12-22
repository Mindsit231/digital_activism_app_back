package mindsit.digitalactivismapp.service.post;

import mindsit.digitalactivismapp.mapper.PostMapper;
import mindsit.digitalactivismapp.model.community.MemberCommunity;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.model.post.LikedPost;
import mindsit.digitalactivismapp.model.post.Post;
import mindsit.digitalactivismapp.model.post.PostImage;
import mindsit.digitalactivismapp.model.post.PostVideo;
import mindsit.digitalactivismapp.model.tag.PostTag;
import mindsit.digitalactivismapp.model.tag.Tag;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.modelDTO.post.PostDTO;
import mindsit.digitalactivismapp.modelDTO.post.PostRequest;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.repository.community.MemberCommunityRepository;
import mindsit.digitalactivismapp.repository.post.LikedPostRepository;
import mindsit.digitalactivismapp.repository.post.PostImageRepository;
import mindsit.digitalactivismapp.repository.post.PostRepository;
import mindsit.digitalactivismapp.repository.post.PostVideoRepository;
import mindsit.digitalactivismapp.repository.tag.PostTagRepository;
import mindsit.digitalactivismapp.service.EntityService;
import mindsit.digitalactivismapp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static mindsit.digitalactivismapp.custom.Functions.getToken;

@Service
public class PostService extends EntityService<Post, PostRepository> {

    private final PostMapper postMapper;
    private final MemberRepository memberRepository;
    private final LikedPostRepository likedPostRepository;
    private final MemberCommunityRepository memberCommunityRepository;
    private final PostTagRepository postTagRepository;

    private final PostImageRepository postImageRepository;
    private final PostVideoRepository postVideoRepository;
    private final TagService tagService;

    @Autowired
    public PostService(PostRepository repository, PostMapper postMapper,
                       MemberRepository memberRepository, LikedPostRepository likedPostRepository,
                       MemberCommunityRepository memberCommunityRepository, PostTagRepository postTagRepository, PostImageRepository postImageRepository,
                       PostVideoRepository postVideoRepository, TagService tagService) {
        super(repository);
        this.postMapper = postMapper;
        this.memberRepository = memberRepository;
        this.likedPostRepository = likedPostRepository;
        this.memberCommunityRepository = memberCommunityRepository;
        this.postTagRepository = postTagRepository;
        this.postImageRepository = postImageRepository;
        this.postVideoRepository = postVideoRepository;
        this.tagService = tagService;
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }

    public List<Post> fetchPostsLimitedByCommunityId(FetchEntityLimited fetchEntityLimited) {
        return entityRepository.fetchPostsLimitedByCommunityId(fetchEntityLimited.limit(), fetchEntityLimited.offset(), fetchEntityLimited.optionalId());
    }

    public List<PostDTO> fetchPostDTOSLimitedByCommunityId(FetchEntityLimited fetchEntityLimited, String authHeader) {
        return getToken(authHeader).map(memberRepository::findByToken)
                .map(member -> postMapper.postToPostDTO(fetchPostsLimitedByCommunityId(fetchEntityLimited), member))
                .orElseGet(ArrayList::new);
    }

    @Transactional
    public Boolean toggleLike(Long postId, String authHeader) {
        return getToken(authHeader).map(memberRepository::findByToken).map(member -> {
            if (likedPostRepository.findByPostIdAndMemberId(postId, member.getId()) != null) {
                likedPostRepository.deleteByPostIdAndMemberId(postId, member.getId());
            } else {
                LikedPost likedPost = new LikedPost(postId, member.getId(), new Date());
                likedPostRepository.save(likedPost);
            }
            return true;
        }).orElse(false);
    }

    public Integer getTableLengthByCommunityId(Long communityId) {
        return entityRepository.getTableLengthByCommunityId(communityId);
    }

    public ResponseEntity<PostDTO> addPost(PostRequest postRequest, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(memberRepository::findByToken);
        if(optionalMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            Member member = optionalMember.get();
            MemberCommunity memberCommunity = memberCommunityRepository.findByCommunityIdAndMemberId(postRequest.communityId(), member.getId());

            if(memberCommunity.getIsAdmin()) {
                Post mappedPost = postMapper.postRequestToPost(postRequest, member);
                Post outputPost = entityRepository.save(mappedPost);

                List<PostImage> postImages = postMapper.postImageRequestToPostImage(postRequest.postImageRequests());
                postImages.forEach(postImage -> postImage.setPostId(outputPost.getId()));
                postImageRepository.saveAll(postImages);

                List<PostVideo> postVideos = postMapper.postVideoRequestToPostVideo(postRequest.postVideoRequests());
                postVideos.forEach(postVideo -> postVideo.setPostId(outputPost.getId()));
                postVideoRepository.saveAll(postVideos);

                List<Tag> tagList = tagService.saveAll(postRequest.tagList());
                List<PostTag> postTagList = new ArrayList<>();
                tagList.forEach(tag -> postTagList.add(new PostTag(outputPost.getId(), tag.getId())));
                postTagRepository.saveAll(postTagList);

                Post fullOutputPost = entityRepository.findById(outputPost.getId()).orElse(null);

                if(fullOutputPost != null) {
                    PostDTO postDTO = postMapper.postToPostDTO(fullOutputPost, member);
                    return ResponseEntity.ok(postDTO);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
    }
}
