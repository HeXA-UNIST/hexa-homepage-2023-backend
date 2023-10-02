package pro.hexa.backend.main.api.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminCreateProjectRequestDto {
    @Schema(description = "프로젝트 제목", required = true)
    private String title;

    @Schema(description = "프로젝트 시작일", required = true)
    private Date startDate;

    @Schema(description = "프로젝트 종료일")
    private Date endDate;

    @Schema(description = "프로젝트 기술 스택", required = true)
    private List<String> projectTechStacks;

    @Schema(description = "프로젝트 상태", required = true)
    private String state;

    @Schema(description = "프로젝트 내용", required = true)
    private String content;

    @Schema(description = "썸네일 이미지 파일 ID", required = true)
    private Long thumbnail;
}