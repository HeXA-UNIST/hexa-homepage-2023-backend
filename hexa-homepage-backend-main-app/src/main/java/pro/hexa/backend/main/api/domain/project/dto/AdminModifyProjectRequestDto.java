package pro.hexa.backend.main.api.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminModifyProjectRequestDto {

    @Schema(description = "수정할 프로젝트 id", required = true)
    private Long projectId;

    @Schema(description = "프로젝트 제목")
    private String title;

    @Schema(description = "프로젝트 시작일")
    private LocalDate startDate;

    @Schema(description = "프로젝트 종료일")
    private LocalDate endDate;

    @Schema(description = "프로젝트 기술 스택")
    private List<String> projectTechStacks;

    @Schema(description = "프로젝트 상태")
    private String state;

    @Schema(description = "프로젝트 내용")
    private String content;

    @Schema(description = "썸네일 이미지 파일 ID")
    private Long thumbnail;
}
