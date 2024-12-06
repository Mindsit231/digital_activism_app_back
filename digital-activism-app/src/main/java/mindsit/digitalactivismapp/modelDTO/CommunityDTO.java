package mindsit.digitalactivismapp.modelDTO;

import java.util.Date;

public record CommunityDTO(
        Long id,
        String name,
        String description,
        String logoName,
        String bannerName,
        Date timestamp,
        boolean joined
) {
}
