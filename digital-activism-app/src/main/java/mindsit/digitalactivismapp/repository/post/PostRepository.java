package mindsit.digitalactivismapp.repository.post;

import mindsit.digitalactivismapp.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query("DELETE FROM Post p WHERE p.id = :id")
    Integer deleteEntityById(Long id);

    @Query("SELECT p FROM Post p WHERE p.communityId = :communityId ORDER by p.timestamp ASC LIMIT :limit OFFSET :offset")
    List<Post> fetchPostsLimitedByCommunityId(Integer limit, Integer offset, Long communityId);
}
