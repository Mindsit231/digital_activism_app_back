package mindsit.digitalactivismapp.modelDTO;

import java.util.Date;

public record CommunityDTO(
        String name,
        String description,
        String logoName,
        byte[] logo,
        String bannerName,
        byte[] banner,
        Date timestamp,
        boolean isMember
) {
}
