package nova.minisite_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class GenerateurJwt {

    private final Key cle;
    private final long expiration;

    public GenerateurJwt(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.cle = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    public String genererToken(String email, String role) {
        Date maintenant = new Date();
        Date fin = new Date(maintenant.getTime() + expiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(maintenant)
                .setExpiration(fin)
                .signWith(cle, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraireEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean estValide(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(cle)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
