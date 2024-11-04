package mindsit.digitalactivismapp.repository.post;

import mindsit.digitalactivismapp.model.post.LikedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikedPostRepository extends JpaRepository<LikedPost, Long> {
    @Modifying
    @Query("DELETE FROM LikedPost p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
