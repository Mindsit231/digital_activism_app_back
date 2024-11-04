package mindsit.digitalactivismapp.repository.post;

import mindsit.digitalactivismapp.model.post.PostVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostVideoRepository extends JpaRepository<PostVideo, Long> {
    @Modifying
    @Query("DELETE FROM PostVideo p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
