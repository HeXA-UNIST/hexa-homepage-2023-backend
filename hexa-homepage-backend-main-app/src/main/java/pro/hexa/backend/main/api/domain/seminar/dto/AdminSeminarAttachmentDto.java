package pro.hexa.backend.main.api.domain.seminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminSeminarAttachmentDto {

    @Schema(description = "파일 id")
    private Long fileId;

    @Schema(description = "파일 이름")
    private String fileName;

    @Schema(description = "파일 용량")
    private long fileSize;
}
