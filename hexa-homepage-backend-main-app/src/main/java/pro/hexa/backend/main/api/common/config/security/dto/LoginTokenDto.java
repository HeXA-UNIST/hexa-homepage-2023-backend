package pro.hexa.backend.main.api.common.config.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginTokenDto {

    private String accessToken;

    private String refreshToken;
}
