package mindsit.digitalactivismapp.modelDTO.campaign;

import java.util.Date;

public record CampaignRequest(String name,
                              String description,
                              Date startDate,
                              Date endDate,
                              Date creationDate,
                              Long communityId,
                              Long memberId) {
}
