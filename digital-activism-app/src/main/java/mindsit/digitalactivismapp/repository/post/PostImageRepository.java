package mindsit.digitalactivismapp.repository.post;

import mindsit.digitalactivismapp.model.post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    @Modifying
    @Query("DELETE FROM PostImage p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
