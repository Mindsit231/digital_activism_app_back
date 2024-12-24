package mindsit.digitalactivismapp.repository.campaign;

import mindsit.digitalactivismapp.model.campaign.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    @Modifying
    @Query("DELETE FROM Campaign p WHERE p.id = :id")
    Integer deleteEntityById(Long id);

    @Query("SELECT COUNT(p) FROM Campaign p WHERE p.communityId = :communityId")
    Integer getTableLengthByCommunityId(Long communityId);

    @Query("SELECT p FROM Campaign p WHERE p.communityId = :communityId ORDER by p.creationDate ASC LIMIT :limit OFFSET :offset")
    List<Campaign> fetchCampaignsLimitedByCommunityId(Integer limit, Integer offset, Long communityId);
}
