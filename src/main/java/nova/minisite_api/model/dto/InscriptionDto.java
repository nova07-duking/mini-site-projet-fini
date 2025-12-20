package nova.minisite_api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import nova.minisite_api.model.Role;

@Getter
@Setter
public class InscriptionDto {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;

    // Utilisation directe de l'Enum
    // Swagger affichera automatiquement un menu de sélection
    private Role role;
}