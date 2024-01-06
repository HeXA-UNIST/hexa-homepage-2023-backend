package pro.hexa.backend.main.api.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.hexa.backend.domain.project.domain.Project;
import pro.hexa.backend.domain.project.model.STATE_TYPE;
import pro.hexa.backend.domain.project_member.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.project_tech_stack.domain.ProjectTechStack;

import static pro.hexa.backend.main.api.common.utils.DateUtils.YYYY_MM_DD;
import static pro.hexa.backend.main.api.common.utils.DateUtils.toFormat;

@Getter
@NoArgsConstructor
public class ProjectDto {

    @Schema(description = "id")
    protected Long projectId;

    @Schema(description = "제목")
    protected String title;

    @Schema(description = "썸네일 url")
    protected String thumbnailUrl;

    @Schema(description = "시작 날짜", example = "2022.01.01")
    protected String startDate;

    @Schema(description = "종료 날짜", defaultValue = "", example = "2022.01.01")
    protected String endDate;

    @Schema(description = "기술스택 리스트")
    protected List<String> techStackList;

    @Schema(description = "멤버 정보 리스트")
    protected List<ProjectMemberDto> memberList;

    @Schema(description = "상태")
    protected STATE_TYPE status;

    @Schema(description = "공개")
    protected AUTHORIZATION_TYPE public_status;

    public void fromProject(Project project) {
        this.projectId = project.getId();
        this.title = project.getTitle();
        this.thumbnailUrl = project.getThumbnail().getLocation();
        this.startDate = toFormat(project.getStartDate(), YYYY_MM_DD);
        this.endDate = toFormat(project.getEndDate(), YYYY_MM_DD);
        this.techStackList = project.getProjectTechStacks().stream()
            .map(ProjectTechStack::getContent)
            .collect(Collectors.toList());
        this.memberList = project.getMembers().stream()
            .map(ProjectMemberDto::fromProjectMember)
            .collect(Collectors.toList());
        this.status = project.getState();
        this.public_status = project.getAuthorization();
    }
}
