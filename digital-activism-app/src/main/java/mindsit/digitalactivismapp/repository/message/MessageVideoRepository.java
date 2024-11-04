package mindsit.digitalactivismapp.repository.message;

import mindsit.digitalactivismapp.model.message.MessageVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MessageVideoRepository extends JpaRepository<MessageVideo, Long> {
    @Modifying
    @Query("DELETE FROM MessageVideo p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
