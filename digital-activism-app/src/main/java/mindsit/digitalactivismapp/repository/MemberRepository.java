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
    @Query("Update Member c SEt c.password = :password WHERE c.token = :token")
    void updatePasswordByToken(@Param("token") String token, @Param("password") String password);

    @Modifying
    @Query("UPDATE Member c SET c.token = :token WHERE c.email = :email")
    void updateTokenByEmail(@Param("email") String email, @Param("token") String token);

    @Modifying
    @Query("UPDATE Member c SET c.pfpName = :pfpName WHERE c.email = :email")
    Integer updatePfpNameByEmail(@Param("email") String email, @Param("pfpName") String pfpName);

    @Modifying
    @Query("Update Member c set c.emailVerified = true WHERE c.email = :email")
    void updateEmailVerifiedByEmail(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM Member p WHERE p.id = :id")
    Integer deleteEntityById(Long id);
}
