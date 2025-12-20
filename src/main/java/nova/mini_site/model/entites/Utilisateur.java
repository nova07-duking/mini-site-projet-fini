package nova.mini_site.model.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nova.mini_site.model.Role;


@Entity
@Getter
@Setter
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    /** Mot de passe HASHÉ (BCrypt) */
    @Column(nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean actif = false;

    /** OTP courant */
    private String otp;

    /** Expiration OTP */
    private Long otpExpiration;
}
