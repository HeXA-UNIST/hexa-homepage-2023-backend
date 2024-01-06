package pro.hexa.backend.main.api.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.hexa.backend.domain.project.domain.Project;
import pro.hexa.backend.domain.project_tech_stack.domain.ProjectTechStack;
import pro.hexa.backend.main.api.common.utils.DateUtils;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProjectDetailResponse {

    @Schema(description = "프로젝트 제목")
    private String title;

    @Schema(description = "프로젝트 시작 날짜")
    private String startDate;

    @Schema(description = "프로젝트 종료 날짜")
    private String endDate;

    @Schema(description = "프로젝트 기술 스택 리스트")
    private List<String> projectTechStacks;

    @Schema(description = "프로젝트 상태")
    private String state;

    @Schema(description = "프로젝트 내용")
    private String content;

    @Schema(description = "썸네일 이미지 파일 id")
    private Long thumbnail;

    public void fromProject(Project project) {
        this.title = project.getTitle();
        this.startDate = DateUtils.toFormat(project.getStartDate(),DateUtils.YYYY_MM_DD);
        this.endDate = DateUtils.toFormat(project.getEndDate(),DateUtils.YYYY_MM_DD);
        this.projectTechStacks = project.getProjectTechStacks().stream()
            .map(ProjectTechStack::getContent)
            .collect(Collectors.toList());
        this.state = String.valueOf(project.getState());
        this.content = project.getContent();
        this.thumbnail = project.getThumbnail().getId();
    }
}
