package nova.minisite_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProduitDto {

    @NotBlank
    private String nom;

    private String description;

    @NotNull
    private BigDecimal prix;

    @NotNull
    private Long categorieId;
}
