package nova.minisite_api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 8, message = "Le mot de passe doit contenir au minimum 8 caract�res")
    private String motDePasse;

    // Utilisation directe de l'Enum
    // Swagger affichera automatiquement un menu de s�lection
    private Role role;
}