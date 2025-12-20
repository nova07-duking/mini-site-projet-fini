package nova.mini_site.repository;

import nova.mini_site.model.entites.RefreshToken;
import nova.mini_site.model.entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUtilisateur(Utilisateur utilisateur);
}
