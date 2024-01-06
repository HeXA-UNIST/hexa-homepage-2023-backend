package pro.hexa.backend.main.api.domain.seminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminModifySeminarRequestDto {

    @Schema(description = "수정할 세미나 id", required = true)
    private Long seminarId;

    @Schema(description = "세미나 제목")
    private String title;

    @Schema(description = "세미나 내용")
    private String content;

    @Schema(description = "세미나 날짜")
    private LocalDateTime date;

    @Schema(description = "첨부파일 id 리스트")
    private List<Long> attachments;
}
