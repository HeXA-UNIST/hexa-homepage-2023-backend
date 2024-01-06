package pro.hexa.backend.main.api.domain.user.domain.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserFindPasswordChangeRequestDto {

    @Schema(description = "새 비밀번호")
    private String password1;

    @Schema(description = "새 비밀번호 확인")
    private String password2;
}
