package nova.minisite_api.repository;

import nova.minisite_api.model.entites.OtpCode;
import nova.minisite_api.model.entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    Optional<OtpCode> findTopByUtilisateurOrderByExpirationDesc(Utilisateur utilisateur);

    void deleteByUtilisateur(Utilisateur utilisateur);
}
