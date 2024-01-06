package pro.hexa.backend.main.api.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminProjectListResponse {

    @Schema(description = "총 페이지 수")
    private int totalPage;

    @Schema(description = "프로젝트 리스트")
    private List<AdminProjectDto> list;
}
