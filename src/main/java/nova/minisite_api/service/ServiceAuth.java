package nova.minisite_api.service;

import lombok.Getter;
import lombok.Setter;
import nova.minisite_api.exceptions.ConflitException;
import nova.minisite_api.exceptions.DonneesInvalidesException;
import nova.minisite_api.exceptions.NonAutoriseException;
import nova.minisite_api.exceptions.RessourceNonTrouveeException;
import nova.minisite_api.model.Role;
import nova.minisite_api.model.dto.*;
import nova.minisite_api.model.entites.OtpCode;
import nova.minisite_api.model.entites.RefreshToken;
import nova.minisite_api.model.entites.Utilisateur;
import nova.minisite_api.repository.OtpCodeRepository;
import nova.minisite_api.repository.RefreshTokenRepository;
import nova.minisite_api.repository.UtilisateurRepository;
import nova.minisite_api.security.GenerateurJwt;
import nova.minisite_api.util.OtpUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Service
public class ServiceAuth {

    private final UtilisateurRepository utilisateurRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OtpCodeRepository otpCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final GenerateurJwt generateurJwt;
    private final EmailService emailService;

    public ServiceAuth(UtilisateurRepository utilisateurRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       OtpCodeRepository otpCodeRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       GenerateurJwt generateurJwt,
                       EmailService emailService) {

        this.utilisateurRepository = utilisateurRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.otpCodeRepository = otpCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.generateurJwt = generateurJwt;
        this.emailService = emailService;
    }

    @Transactional
    public void inscription(InscriptionDto dto) {
        if (utilisateurRepository.existsByEmail(dto.getEmail())) {
            throw new ConflitException("Email déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));

        // Mapping direct : si dto.getRole() est null, on met CLIENT
        utilisateur.setRole(dto.getRole() != null ? dto.getRole() : Role.CLIENT);

        utilisateur.setActif(false);
        utilisateurRepository.save(utilisateur);
        creerEtEnvoyerOtp(utilisateur);
    }

    @Transactional
    public void verifierOtp(VerificationOtpDto dto) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RessourceNonTrouveeException("Utilisateur introuvable"));

        OtpCode otp = otpCodeRepository
                .findTopByUtilisateurOrderByExpirationDesc(utilisateur)
                .orElseThrow(() -> new DonneesInvalidesException("OTP inexistant"));

        if (!otp.getCode().equals(dto.getOtp())) {
            throw new DonneesInvalidesException("OTP incorrect");
        }

        if (otp.getExpiration() < System.currentTimeMillis()) {
            throw new DonneesInvalidesException("OTP expiré");
        }

        utilisateur.setActif(true);
        utilisateurRepository.save(utilisateur);
        otpCodeRepository.deleteByUtilisateur(utilisateur);
    }

    @Transactional
    public void demanderOtp(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RessourceNonTrouveeException("Utilisateur introuvable"));

        if (utilisateur.isActif()) {
            throw new DonneesInvalidesException("Compte déjà activé");
        }
        creerEtEnvoyerOtp(utilisateur);
    }

    @Transactional
    public AuthResponseDto login(AuthRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getMotDePasse())
        );

        Utilisateur utilisateur = utilisateurRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RessourceNonTrouveeException("Utilisateur introuvable"));

        if (!utilisateur.isActif()) {
            throw new NonAutoriseException("Compte non activé.");
        }

        String accessToken = generateurJwt.genererToken(utilisateur.getEmail(), utilisateur.getRole().name());
        RefreshToken refreshToken = creerRefreshToken(utilisateur);

        return new AuthResponseDto(accessToken, refreshToken.getToken(), "Bearer");
    }

    @Transactional
    public AuthResponseDto rafraichirToken(RefreshTokenDto dto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(dto.getRefreshToken())
                .orElseThrow(() -> new NonAutoriseException("Refresh token invalide"));

        if (refreshToken.getDateExpiration().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new NonAutoriseException("Refresh token expiré");
        }

        Utilisateur utilisateur = refreshToken.getUtilisateur();
        String nouveauAccessToken = generateurJwt.genererToken(utilisateur.getEmail(), utilisateur.getRole().name());

        return new AuthResponseDto(nouveauAccessToken, refreshToken.getToken(), "Bearer");
    }

    @Transactional
    public void logout(String refreshTokenToken) {
        refreshTokenRepository.findByToken(refreshTokenToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    private void creerEtEnvoyerOtp(Utilisateur utilisateur) {
        otpCodeRepository.deleteByUtilisateur(utilisateur);

        OtpCode otp = new OtpCode();
        otp.setCode(OtpUtils.genererOtp());
        otp.setExpiration(System.currentTimeMillis() + 5 * 60 * 1000);
        otp.setUtilisateur(utilisateur);
        otpCodeRepository.save(otp);

        emailService.envoyerEmailOtp(utilisateur.getEmail(), otp.getCode());
        System.out.println("OTP envoyé par email à : " + utilisateur.getEmail() + " | Code : " + otp.getCode());
    }

    private RefreshToken creerRefreshToken(Utilisateur utilisateur) {
        refreshTokenRepository.deleteByUtilisateur(utilisateur);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUtilisateur(utilisateur);
        refreshToken.setDateExpiration(Instant.now().plusSeconds(7 * 24 * 60 * 60));
        return refreshTokenRepository.save(refreshToken);
    }
}