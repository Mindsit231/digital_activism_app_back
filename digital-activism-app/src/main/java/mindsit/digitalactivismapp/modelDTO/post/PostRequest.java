package mindsit.digitalactivismapp.modelDTO.post;

import java.util.List;

public record PostRequest(String title,
                          String content,
                          String visibility,
                          Long communityId,
                          Long memberId,

                          List<PostImageRequest> postImageRequests,
                          List<PostVideoRequest> postVideoRequests,
                          List<String> tagList) {
}
