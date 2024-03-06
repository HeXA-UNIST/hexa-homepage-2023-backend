package pro.hexa.backend.main.api.domain.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AdminServiceDto {

    @Schema(description = "서비스 id")
    private Long serviceId;

    @Schema(description = "서비스 제목")
    private String title;

    @Schema(description = "이미지 파일 id")
    private Long thumbnail;
}
