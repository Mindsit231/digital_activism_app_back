package mindsit.digitalactivismapp.repository.community;

import mindsit.digitalactivismapp.model.community.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    @Modifying
    @Query("DELETE FROM Community p WHERE p.id = :id")
    Integer deleteEntityById(Long id);

    @Query("SELECT COUNT(p) FROM Community p")
    Integer getTableLength();

    @Query("SELECT COUNT(c) FROM Community c WHERE LOWER(c.name) LIKE %:searchValue%")
    Integer getTableLengthBySearchValue(String searchValue);

    @Query("SELECT c FROM Community c ORDER BY c.timestamp DESC LIMIT :limit OFFSET :offset")
    List<Community> fetchCommunitiesLimited(Integer limit, Integer offset);

    @Query("SELECT c FROM Community c WHERE LOWER(c.name) LIKE %:searchValue% ORDER BY c.timestamp DESC LIMIT :limit OFFSET :offset")
    List<Community> fetchCommunitiesLimitedBySearchValue(Integer limit, Integer offset, String searchValue);

    @Query("SELECT c FROM Community c " +
            "INNER JOIN MemberCommunity mc ON c.id = mc.communityId " +
            "INNER JOIN Member m ON mc.memberId = m.id " +
            "WHERE mc.memberId = :memberId " +
            "ORDER BY c.timestamp DESC LIMIT :limit OFFSET :offset")
    List<Community> fetchCommunitiesLimitedByMemberId(Integer limit, Integer offset, Long memberId);

    @Query("SELECT COUNT(c) FROM Community c " +
            "INNER JOIN MemberCommunity mc ON c.id = mc.communityId " +
            "INNER JOIN Member m ON mc.memberId = m.id " +
            "WHERE mc.memberId = :memberId")
    Integer fetchCommunitiesCountByMemberId(Long memberId);
}
