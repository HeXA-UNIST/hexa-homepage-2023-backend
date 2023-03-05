package pro.hexa.backend.main.api.common.config.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LoginUserDto {
    @Schema(description = "이름")
    private String userId;

    @Schema(description = "비번")
    private String password;
}
