package mindsit.digitalactivismapp.modelDTO.post;

import mindsit.digitalactivismapp.model.post.Visibility;
import mindsit.digitalactivismapp.model.tag.Tag;

import java.util.List;

public record PostDTO (
        Long id,
        String title,
        String content,
        Visibility visibility,
        Long communityId,
        List<PostImageDTO> postImageDTOS,
        List<PostVideoDTO> postVideoDTOS,
        List<Tag> tags
){
}
