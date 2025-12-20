package nova.minisite_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorieDto {

    @NotBlank
    private String nom;

    private String description;
}
