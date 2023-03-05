package pro.hexa.backend.main.api.domain.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDto {
    @Schema(description = "유저 이름")
    private String id;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "성별, 0 : male | 1 : female")
    private int gender;

    @Schema(description = "재학중 여부, 0 : 재학  |  1 : 휴학  |  2 : 졸업")
    private int state;

    @Schema(description = "입학년도")
    private String regYear;

    @Schema(description = "학번, 없을 시 null이 아닌 빈 문자열로.")
    private String regNum;

    @Schema(description = "이름", example = "김선욱")
    private String name;

    @Schema(description = "비번1")
    private String password1;

    @Schema(description = "비번2")
    private String password2;
}