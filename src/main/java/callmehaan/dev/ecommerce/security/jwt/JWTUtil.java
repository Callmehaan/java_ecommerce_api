package callmehaan.dev.ecommerce.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JWTUtil {
    private static final String SECRET_KEY =
            "4a2aaefc-8077-449d-930e-0406e51bd64a-4a2aaefc-8077-449d-930e-0406e51bd64a";

    public String issueToken(String subject) {
        return this.issueToken(subject, Map.of());
    }

    public String issueToken(String subject, String ...scopes) {
        return this.issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(String subject, List<String> scopes) {
        return this.issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(
            String subject,
            Map<String, Object> claims
    ) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer("http://localhost:5173")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(15, ChronoUnit.DAYS)))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigninKey() {
        return Keys
                .hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String jwt, String username) {
        String subject = getSubject(jwt);

        return subject.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        Date today = Date.from(Instant.now());

        return getClaims(jwt).getExpiration().before(today);
    }
}
