package nova.minisite_api.service;

import nova.minisite_api.exceptions.NonAutoriseException;
import nova.minisite_api.model.entites.RefreshToken;
import nova.minisite_api.model.entites.Utilisateur;
import nova.minisite_api.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ServiceRefreshToken {

    private final RefreshTokenRepository repository;

    public ServiceRefreshToken(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken creer(Utilisateur utilisateur) {
        repository.deleteByUtilisateur(utilisateur);

        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUtilisateur(utilisateur);
        token.setDateExpiration(Instant.now().plusSeconds(7 * 24 * 3600));
        // Correction ici : setRevoked(false)
        token.setRevoked(false);

        return repository.save(token);
    }

    public RefreshToken verifier(String token) {
        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new NonAutoriseException("Refresh token invalide"));

        // Correction ici : isRevoked() et getDateExpiration()
        if (refreshToken.isRevoked() ||
                refreshToken.getDateExpiration().isBefore(Instant.now())) {
            throw new NonAutoriseException("Refresh token expiré");
        }

        return refreshToken;
    }
}