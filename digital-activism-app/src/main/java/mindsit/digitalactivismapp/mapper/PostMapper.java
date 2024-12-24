package mindsit.digitalactivismapp.mapper;

import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.model.post.LikedPost;
import mindsit.digitalactivismapp.model.post.Post;
import mindsit.digitalactivismapp.model.post.PostImage;
import mindsit.digitalactivismapp.model.post.PostVideo;
import mindsit.digitalactivismapp.model.tag.Tag;
import mindsit.digitalactivismapp.modelDTO.post.*;
import mindsit.digitalactivismapp.repository.post.LikedPostRepository;
import mindsit.digitalactivismapp.repository.post.PostRepository;
import mindsit.digitalactivismapp.repository.tag.TagRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    @Autowired
    protected PostRepository postRepository;
    @Autowired
    protected TagRepository tagRepository;
    @Autowired
    protected LikedPostRepository likedPostRepository;
    @Autowired
    protected MemberMapper memberMapper;

    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "memberDTO", expression = "java(memberMapper.memberToMemberDTOShort(post.getMember()))")
    @Mapping(target = "postImageDTOS", expression = "java(postImageToPostImageDTO(post.getPostImages()))")
    @Mapping(target = "postVideoDTOS", expression = "java(postVideoToPostVideoDTO(post.getPostVideos()))")
    @Mapping(target = "tagList", expression = "java(fetchTagsByPostId(post.getId()))")
    @Mapping(target = "creationDate", source = "post.creationDate")
    @Mapping(target = "likesCount", expression = "java(fetchLikesCount(post.getId()))")
    @Mapping(target = "liked", expression = "java(isLiked(post.getId(), member.getId()))")
    public abstract PostDTO postToPostDTO(Post post, Member member);

    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "memberDTO", expression = "java(memberMapper.memberToMemberDTOShort(post.getMember()))")
    @Mapping(target = "postImageDTOS", expression = "java(postImageToPostImageDTO(post.getPostImages()))")
    @Mapping(target = "postVideoDTOS", expression = "java(postVideoToPostVideoDTO(post.getPostVideos()))")
    @Mapping(target = "tagList", expression = "java(fetchTagsByPostId(post.getId()))")
    @Mapping(target = "creationDate", source = "post.creationDate")
    @Mapping(target = "likesCount", expression = "java(fetchLikesCount(post.getId()))")
    @Mapping(target = "liked", expression = "java(false)")
    public abstract PostDTO postToPostDTO(Post post);

    public List<PostDTO> postToPostDTO(List<Post> posts, Member member) {
        return posts.stream().map(post -> postToPostDTO(post, member)).toList();
    }

    public List<PostDTO> postToPostDTO(List<Post> posts) {
        return posts.stream().map(this::postToPostDTO).toList();
    }

    public abstract PostImageDTO postImageToPostImageDTO(PostImage postImage);

    public abstract PostVideoDTO postVideoToPostVideoDTO(PostVideo postVideo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visibility", expression = "java(Visibility.find(postRequest.visibility()))")
    @Mapping(target = "creationDate", expression = "java(new Date())")
    public abstract Post postRequestToPost(PostRequest postRequest, Member member);

    public List<PostImageDTO> postImageToPostImageDTO(Collection<PostImage> postImages) {
        List<PostImageDTO> postImageDTOS = new ArrayList<>();
        for (PostImage postImage : postImages) {
            postImageDTOS.add(postImageToPostImageDTO(postImage));
        }
        return postImageDTOS;
    }

    public List<PostVideoDTO> postVideoToPostVideoDTO(Collection<PostVideo> postVideos) {
        List<PostVideoDTO> postVideoDTOS = new ArrayList<>();
        for (PostVideo postVideo : postVideos) {
            postVideoDTOS.add(postVideoToPostVideoDTO(postVideo));
        }
        return postVideoDTOS;
    }

    public List<PostImage> postImageRequestToPostImage(List<PostImageRequest> postImageRequests) {
        List<PostImage> postImages = new ArrayList<>();
        for (PostImageRequest postImageRequest : postImageRequests) {
            postImages.add(new PostImage(postImageRequest.name()));
        }
        return postImages;
    }

    public List<PostVideo> postVideoRequestToPostVideo(List<PostVideoRequest> postVideoRequests) {
        List<PostVideo> postVideos = new ArrayList<>();
        for (PostVideoRequest postVideoRequest : postVideoRequests) {
            postVideos.add(new PostVideo(postVideoRequest.name()));
        }
        return postVideos;
    }

    public List<Tag> fetchTagsByPostId(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        List<Tag> tagList = new ArrayList<>();
        optionalPost.ifPresent(post -> post.getPostTags().forEach(postTag -> {
            Optional<Tag> optionalTag = tagRepository.findById(postTag.getTagId());
            optionalTag.ifPresent(tagList::add);
        }));
        return tagList;
    }

    public int fetchLikesCount(Long id) {
        List<LikedPost> likedPosts = likedPostRepository.findByPostId(id);
        return likedPosts.size();
    }

    public boolean isLiked(Long postId, Long memberId) {
        return likedPostRepository.findByPostIdAndMemberId(postId, memberId) != null;
    }
}
