package pro.hexa.backend.main.api.domain.seminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminCreateSeminarRequestDto {

    @Schema(description = "세미나 제목", required = true)
    private String title;

    @Schema(description = "세미나 내용", required = true)
    private String content;

    @Schema(description = "세미나 날짜", required = true)
    private Date date;

    @Schema(description = "첨부파일 id 리스트")
    private List<Long> attachments;
}
