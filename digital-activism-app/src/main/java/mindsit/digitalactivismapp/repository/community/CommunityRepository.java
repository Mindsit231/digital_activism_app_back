package mindsit.digitalactivismapp.repository.community;

import mindsit.digitalactivismapp.model.community.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    @Modifying
    @Query("DELETE FROM Community p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
