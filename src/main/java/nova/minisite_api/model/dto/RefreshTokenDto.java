package nova.minisite_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDto {

    @NotBlank
    private String refreshToken;
}
