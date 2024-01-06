package pro.hexa.backend.main.api.domain.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminModifyServiceRequestDto {

    @Schema(description = "수정할 서비스 id", required = true)
    private Long serviceId;

    @Schema(description = "서비스 이름")
    private String title;

    @Schema(description = "서비스 설명")
    private String content;

    @Schema(description = "썸네일 이미지 파일 id")
    private Long thumbnail;

    @Schema(description = "서비스 사이트 주소")
    private String siteLink;

    @Schema(description = "서비스 깃헙 주소")
    private String githubLink;


}
