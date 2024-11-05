package mindsit.digitalactivismapp.repository;

import mindsit.digitalactivismapp.model.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);

    Member findByToken(String token);

    @Modifying
    @Query("Update Member c SET c.password = :password WHERE c.email = :email")
    Integer updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

    @Modifying
    @Query("UPDATE Member c SET c.token = :token WHERE c.email = :email")
    Integer updateTokenByEmail(@Param("email") String email, @Param("token") String token);

    @Modifying
    @Query("UPDATE Member c SET c.token = :newToken WHERE c.token = :oldToken")
    Integer updateTokenByOldToken(@Param("oldToken") String oldToken, @Param("newToken") String newToken);

    @Modifying
    @Query("UPDATE Member c SET c.pfpName = :pfpImgPath WHERE c.email = :email")
    Integer updatePfpNameByEmail(@Param("email") String email, @Param("pfpImgPath") String pfpImgPath);

    @Modifying
    @Query("Update Member c set c.emailVerified = true WHERE c.email = :email")
    Integer updateEmailVerifiedByEmail(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM Member p WHERE p.id = :id")
    Integer deleteEntityById(Long id);

    @Query("SELECT m FROM Member m WHERE m.username LIKE %:username%")
    List<Member> findMembersByUsername(String username);
}
