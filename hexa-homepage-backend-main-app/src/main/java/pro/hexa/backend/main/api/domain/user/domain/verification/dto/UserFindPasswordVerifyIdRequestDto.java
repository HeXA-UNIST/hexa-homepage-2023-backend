package pro.hexa.backend.main.api.domain.user.domain.verification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserFindPasswordVerifyIdRequestDto {

    @Schema(description = "아이디")
    private String id;

    @Schema(description = "인증번호")
    private String VerificationCode;
}
