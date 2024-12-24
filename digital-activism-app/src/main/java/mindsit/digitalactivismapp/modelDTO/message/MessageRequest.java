package mindsit.digitalactivismapp.modelDTO.message;

public record MessageRequest(
        String text,
        Long campaignId,
        Long memberId) {
}
