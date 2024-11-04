package mindsit.digitalactivismapp.repository.message;

import mindsit.digitalactivismapp.model.message.MessageImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MessageImageRepository extends JpaRepository<MessageImage, Long> {
    @Modifying
    @Query("DELETE FROM MessageImage p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
