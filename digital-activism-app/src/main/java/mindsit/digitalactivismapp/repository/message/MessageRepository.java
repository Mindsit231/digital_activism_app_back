package mindsit.digitalactivismapp.repository.message;

import mindsit.digitalactivismapp.model.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Modifying
    @Query("DELETE FROM Message p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
