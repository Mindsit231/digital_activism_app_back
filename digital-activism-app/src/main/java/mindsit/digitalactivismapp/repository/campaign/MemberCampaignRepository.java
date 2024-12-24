package mindsit.digitalactivismapp.repository.campaign;

import mindsit.digitalactivismapp.model.campaign.MemberCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberCampaignRepository extends JpaRepository<MemberCampaign, Long> {
    @Modifying
    @Query("DELETE FROM MemberCommunity p WHERE p.id = :id")
    Integer deleteEntityById(Long id);

    @Modifying
    @Query("DELETE FROM MemberCampaign p WHERE p.campaignId = :campaignId AND p.memberId = :memberId")
    void deleteByCampaignIdAndMemberId(Long campaignId, Long memberId);

    @Query("SELECT p FROM MemberCampaign p WHERE p.campaignId = :campaignId AND p.memberId = :memberId")
    MemberCampaign findByCampaignIdAndMemberId(Long campaignId, Long memberId);
}
