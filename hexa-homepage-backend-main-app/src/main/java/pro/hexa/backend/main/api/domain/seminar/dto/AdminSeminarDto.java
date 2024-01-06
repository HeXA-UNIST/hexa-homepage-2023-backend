package pro.hexa.backend.main.api.domain.seminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.hexa.backend.domain.project.domain.Project;
import pro.hexa.backend.domain.seminar.domain.Seminar;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminSeminarDto {

    @Schema(description = "세미나 id")
    private Long seminarId;

    @Schema(description = "세미나 제목")
    private String title;

    @Schema(description = "세미나 날짜")
    private LocalDateTime date;

    @Schema(description = "첨부파일 개수")
    private int attachmentsCount;

    public void fromSeminar(Seminar seminar) {
        this.seminarId = seminar.getId();
        this.title = seminar.getTitle();
        this.date = seminar.getDate();
        this.attachmentsCount = seminar.getAttachments().size();
    }
}
