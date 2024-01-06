package pro.hexa.backend.main.api.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.hexa.backend.domain.project.domain.Project;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminProjectDto {

    @Schema(description = "프로젝트 id")
    private Long projectId;

    @Schema(description = "이미지 파일 id")
    private Long thumbnail;

    @Schema(description = "프로젝트 제목")
    private String title;

    @Schema(description = "상태")
    private String state;

    public void fromProject(Project project) {
        this.projectId = project.getId();
        this.title = project.getTitle();
        this.thumbnail = project.getThumbnail().getId();
        this.state = project.getState().getValue();
    }
}
