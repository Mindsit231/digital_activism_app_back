package mindsit.digitalactivismapp.repository.tag;

import mindsit.digitalactivismapp.model.tag.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    @Modifying
    @Query("DELETE FROM PostTag p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
