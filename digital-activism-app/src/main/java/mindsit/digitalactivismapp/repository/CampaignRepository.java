package mindsit.digitalactivismapp.repository;

import mindsit.digitalactivismapp.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    @Modifying
    @Query("DELETE FROM Campaign p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
