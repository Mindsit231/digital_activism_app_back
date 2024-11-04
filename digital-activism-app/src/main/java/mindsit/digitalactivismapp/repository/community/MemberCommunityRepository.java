package mindsit.digitalactivismapp.repository.community;

import mindsit.digitalactivismapp.model.community.MemberCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberCommunityRepository extends JpaRepository<MemberCommunity, Long> {
    @Modifying
    @Query("DELETE FROM MemberCommunity p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
