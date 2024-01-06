package pro.hexa.backend.main.api.domain.user.domain.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    @Schema(description = "이름")
    private String id;

    @Schema(description = "액세스 토큰 bearer")
    private String accessToken;

    @Schema(description = "리프레시 토큰 bearer")
    private String refreshToken;
}
