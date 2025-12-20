package nova.minisite_api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationOtpDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String otp;
}
