package pro.hexa.backend.main.api.domain.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserFindIdRequestDto {
    @Schema(description = "이름")
    private String name;

    @Schema(description = "e-mail")
    private String email;
}
