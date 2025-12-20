package nova.mini_site.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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
        // Utilisation de StandardCharsets.UTF_8 pour éviter les problèmes selon l'OS
        // La clé doit faire au moins 32 caractères (256 bits)
        this.cle = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
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

    public String extraireRole(String token) {
        return getClaims(token).get( "role", String.class);
    }

    public boolean estValide(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            // Token expiré
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            // Token corrompu ou invalide
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