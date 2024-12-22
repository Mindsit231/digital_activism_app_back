package mindsit.digitalactivismapp.repository.community;

import mindsit.digitalactivismapp.model.community.MemberCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberCommunityRepository extends JpaRepository<MemberCommunity, Long> {
    @Modifying
    @Query("DELETE FROM MemberCommunity p WHERE p.id = :id")
    Integer deleteEntityById(Long id);

    @Modifying
    @Query("DELETE FROM MemberCommunity p WHERE p.communityId = :communityId AND p.memberId = :memberId")
    void deleteByCommunityIdAndMemberId(Long communityId, Long memberId);

    @Query("SELECT p FROM MemberCommunity p WHERE p.communityId = :communityId AND p.memberId = :memberId")
    MemberCommunity findByCommunityIdAndMemberId(Long communityId, Long memberId);

    @Query("SELECT p.isAdmin FROM MemberCommunity p WHERE p.communityId = :communityId AND p.memberId = :memberId")
    Boolean findIsAdminByCommunityIdAndMemberId(Long communityId, Long memberId);
}
