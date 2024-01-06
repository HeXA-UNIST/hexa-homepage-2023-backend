package pro.hexa.backend.main.api.domain.seminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminSeminarListResponse {

    @Schema(description = "총 페이지 수")
    private int totalPage;

    @Schema(description = "세미나 리스트")
    private List<AdminSeminarDto> list;
}
