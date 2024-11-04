package mindsit.digitalactivismapp.repository.tag;

import mindsit.digitalactivismapp.model.tag.MemberTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberTagRepository extends JpaRepository<MemberTag, Long> {
    @Modifying
    @Query("DELETE FROM MemberTag p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
