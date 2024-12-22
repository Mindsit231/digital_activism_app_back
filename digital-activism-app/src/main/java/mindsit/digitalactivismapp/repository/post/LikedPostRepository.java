package mindsit.digitalactivismapp.repository.post;

import mindsit.digitalactivismapp.model.post.LikedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikedPostRepository extends JpaRepository<LikedPost, Long> {
    @Modifying
    @Query("DELETE FROM LikedPost p WHERE p.id = :id")
    Integer deleteEntityById(Long id);

    @Query("SELECT p FROM LikedPost p WHERE p.postId = :id")
    List<LikedPost> findByPostId(Long id);

    @Query("SELECT p FROM LikedPost p WHERE p.postId = :postId AND p.memberId = :memberId")
    LikedPost findByPostIdAndMemberId(Long postId, Long memberId);

    @Modifying
    @Query("DELETE FROM LikedPost p WHERE p.postId = :postId AND p.memberId = :memberId")
    void deleteByPostIdAndMemberId(Long postId, Long memberId);
}
