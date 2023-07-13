package pro.hexa.backend.main.api.domain.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindIdWithCodeResponse {
    @Schema(description = "에러 여부")
    private int error;

    @Schema(description = "사용자 id")
    private String userId;
}
