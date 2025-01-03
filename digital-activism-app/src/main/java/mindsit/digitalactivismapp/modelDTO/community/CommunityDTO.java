package mindsit.digitalactivismapp.modelDTO.community;

import java.util.Date;

public record CommunityDTO(
        Long id,
        String name,
        String description,
        String logoName,
        String bannerName,
        Date timestamp,
        Boolean isAdmin,
        Boolean joined
) {
}
