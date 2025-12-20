package nova.minisite_api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO minimal pour demander (ou renvoyer) un OTP.
 */
@Getter
@Setter
public class OtpRequestDto {

    @Email
    @NotBlank
    private String email;
}
