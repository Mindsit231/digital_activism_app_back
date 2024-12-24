package mindsit.digitalactivismapp.repository.message;

import mindsit.digitalactivismapp.model.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Modifying
    @Query("DELETE FROM Message p WHERE p.id = :id")
    Integer deleteEntityById(Long id);

    @Query("SELECT COUNT(p) FROM Message p WHERE p.campaignId = :campaignId")
    Integer getTableLengthByCampaignId(Long campaignId);

    @Query("SELECT p FROM Message p WHERE p.campaignId = :campaignId ORDER by p.timestamp DESC LIMIT :limit OFFSET :offset")
    List<Message> fetchLatestMessagesLimitedByCampaignId(Integer limit, Integer offset, Long campaignId);
}
