package nova.mini_site.repository;

import nova.mini_site.model.entites.OtpCode;
import nova.mini_site.model.entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    Optional<OtpCode> findTopByUtilisateurOrderByExpirationDesc(Utilisateur utilisateur);

    void deleteByUtilisateur(Utilisateur utilisateur);
}
