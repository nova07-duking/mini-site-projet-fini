package nova.mini_site.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nova.mini_site.model.entites.RefreshToken;

@Getter
@AllArgsConstructor
public class AuthResponseDto {

    private String accessToken;
    private String refreshToken;
    private String type;

    public AuthResponseDto(String nouveauAccessToken, String token) {
    }

    public AuthResponseDto(String accessToken, RefreshToken refreshToken, String bearer) {
    }
}

