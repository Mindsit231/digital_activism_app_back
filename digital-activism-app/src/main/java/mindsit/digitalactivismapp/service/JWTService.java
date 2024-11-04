package mindsit.digitalactivismapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import mindsit.digitalactivismapp.model.member.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {
    @Value("${custom.spring.security.secret-key:}")
    private String secretKey;

    public JWTService() {
        // TODO - ENABLE THIS IN PRODUCTION FOR A RANDOM KEY GENERATION ON EVERY STARTUP
//        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = keyGenerator.generateKey();
//            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
    }

    public String generateToken(Member foundMember) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", foundMember.getEmail());
        claims.put("username", foundMember.getUsername());
        claims.put("role", foundMember.getRole());

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(foundMember.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)) // 1 month
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractSub(String token) {
        return extractClaim(token, Claims::getSubject).orElse("");
    }

    public String extractClaim(String token, String claim) {
        return extractClaim(token, claims -> claims.get(claim, String.class)).orElse("");
    }

    private <T> Optional<T> extractClaim(String token, Function<Claims, T> claimResolver) {
        if(extractAllClaims(token).isEmpty()) {
            return Optional.empty();
        } else {
            final Claims claims = extractAllClaims(token).get();
            return Optional.of(claimResolver.apply(claims));
        }
    }

    private Optional<Claims> extractAllClaims(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractSub(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration).orElse(null);
    }
}
