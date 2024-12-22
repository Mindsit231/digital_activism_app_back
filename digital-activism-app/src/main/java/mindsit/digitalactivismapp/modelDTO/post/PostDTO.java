package mindsit.digitalactivismapp.modelDTO.post;

import mindsit.digitalactivismapp.model.post.Visibility;
import mindsit.digitalactivismapp.model.tag.Tag;
import mindsit.digitalactivismapp.modelDTO.member.MemberDTOShort;

import java.util.Date;
import java.util.List;

public record PostDTO(
        Long id,
        String title,
        String content,
        Visibility visibility,
        Date creationDate,
        Long communityId,
        MemberDTOShort memberDTOShort,
        List<PostImageDTO> postImageDTOS,
        List<PostVideoDTO> postVideoDTOS,
        List<Tag> tagList,
        int likesCount,
        boolean liked
) {
}
