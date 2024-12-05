package mindsit.digitalactivismapp.modelDTO.post;

public record PostImageDTO(
        Long id,
        String name,
        Long postId,
        byte[] image
) {
}
