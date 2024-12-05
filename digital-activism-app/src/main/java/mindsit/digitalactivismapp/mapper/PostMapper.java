package mindsit.digitalactivismapp.mapper;

import mindsit.digitalactivismapp.model.post.Post;
import mindsit.digitalactivismapp.model.post.PostImage;
import mindsit.digitalactivismapp.model.post.PostVideo;
import mindsit.digitalactivismapp.modelDTO.post.PostDTO;
import mindsit.digitalactivismapp.modelDTO.post.PostImageDTO;
import mindsit.digitalactivismapp.modelDTO.post.PostVideoDTO;
import mindsit.digitalactivismapp.service.misc.FileService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    protected final FileService fileService = new FileService();

    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "postImageDTOS", expression = "java(postImageToPostImageDTO(post.getPostImages()))")
    @Mapping(target = "postVideoDTOS", expression = "java(postVideoToPostVideoDTO(post.getPostVideos()))")
    public abstract PostDTO postToPostDTO(Post post);

    public List<PostDTO> postToPostDTO(Collection<Post> posts) {
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : posts) {
            postDTOS.add(postToPostDTO(post));
        }
        return postDTOS;
    }

    @Mapping(target = "image", expression = "java(fileService.getResource(postImage.getName(), PostImage.class.getSimpleName().toLowerCase()))")
    public abstract PostImageDTO postImageToPostImageDTO(PostImage postImage);

//    @Mapping(target = "video", expression = "java(fileService.getResource(postVideo.getName(), PostVideo.class.getSimpleName().toLowerCase()))")
    public abstract PostVideoDTO postVideoToPostVideoDTO(PostVideo postVideo);

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
}
