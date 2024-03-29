package pro.hexa.backend.main.api.domain.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFindPasswordRequestDto3 {

    @Schema(description = "새 비밀번호")
    private String password1;

    @Schema(description = "새 비밀번호 확인")
    private String password2;
}
