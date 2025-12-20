package nova.minisite_api.repository;

import nova.minisite_api.model.entites.RefreshToken;
import nova.minisite_api.model.entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUtilisateur(Utilisateur utilisateur);
}
