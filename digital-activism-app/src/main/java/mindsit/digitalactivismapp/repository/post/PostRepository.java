package mindsit.digitalactivismapp.repository.post;

import mindsit.digitalactivismapp.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query("DELETE FROM Post p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
