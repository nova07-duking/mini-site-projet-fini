package nova.minisite_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nova.minisite_api.model.dto.*;
import nova.minisite_api.service.ServiceAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentification", description = "Gestion de l'inscription et de l'authentification")
public class AuthController {

    private final ServiceAuth serviceAuth;

    public AuthController(ServiceAuth serviceAuth) {
        this.serviceAuth = serviceAuth;
    }

    /* =========================
       INSCRIPTION
       ========================= */
    @Operation(summary = "Créer un compte (register)")
    @PostMapping("/register")
    public ResponseEntity<?> inscription(@Valid @RequestBody InscriptionDto dto) {
        serviceAuth.inscription(dto);
        return ResponseEntity.ok("Inscription réussie. OTP envoyé par email.");
    }

    /* =========================
       RENVOI / DEMANDE OTP
       ========================= */
    @Operation(summary = "Demander / renvoyer un OTP")
    @PostMapping("/otp/request")
    public ResponseEntity<?> demanderOtp(@Valid @RequestBody OtpRequestDto dto) {
        serviceAuth.demanderOtp(dto.getEmail());
        return ResponseEntity.ok("OTP envoyé par email.");
    }

    /* =========================
       ACTIVATION OTP
       ========================= */
    @Operation(summary = "Vérifier un OTP (activation)")
    @PostMapping("/otp/verify")
    public ResponseEntity<?> verifierOtp(@Valid @RequestBody VerificationOtpDto dto) {
        serviceAuth.verifierOtp(dto);
        return ResponseEntity.ok("Compte activé avec succès.");
    }

    /* =========================
       LOGIN
       ========================= */
    @Operation(summary = "Connexion utilisateur")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody AuthRequestDto dto) {

        return ResponseEntity.ok(serviceAuth.login(dto));
    }

    /* =========================
       REFRESH TOKEN
       ========================= */
    @Operation(summary = "Rafraîchir le token JWT")
    @PostMapping("/token/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(
            @Valid @RequestBody RefreshTokenDto dto) {

        return ResponseEntity.ok(serviceAuth.rafraichirToken(dto));
    }

    /* =========================
       LOGOUT
       ========================= */
    @Operation(summary = "Déconnexion utilisateur")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenDto dto) {
        serviceAuth.logout(dto.getRefreshToken());
        return ResponseEntity.ok("Déconnexion réussie.");
    }
}
