package mindsit.digitalactivismapp.repository.tag;

import mindsit.digitalactivismapp.model.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    @Modifying
    @Query("DELETE FROM Tag p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
