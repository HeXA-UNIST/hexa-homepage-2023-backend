package pro.hexa.backend.main.api.domain.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminNewsListResponse {

    @Schema(description = "총 페이지 수")
    private int totalPage;

    @Schema(description = "뉴스 리스트")
    private List<AdminNewsDto> list;
}
