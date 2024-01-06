package pro.hexa.backend.main.api.domain.news.controller;

import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.hexa.backend.main.api.domain.news.dto.AdminCreateNewsRequestDto;
import pro.hexa.backend.main.api.domain.news.dto.AdminModifyNewsRequestDto;
import pro.hexa.backend.main.api.domain.news.dto.AdminNewsDetailResponse;
import pro.hexa.backend.main.api.domain.news.dto.AdminNewsListResponse;
import pro.hexa.backend.main.api.domain.news.service.NewsAdminPageService;

@RestController
@RequestMapping("/admin/news")
@RequiredArgsConstructor
public class NewsAdminPageController {
    private final NewsAdminPageService newsAdminPageService;

    @Operation(description = "뉴스 리스트 조회")
    @GetMapping("/list")
    public ResponseEntity<AdminNewsListResponse> getAdminNewsList(
        @RequestParam(required = false) Integer pageNum,
        @RequestParam(required = false) @Valid @Min(value = 1) Integer perPage
    ) {
        return new ResponseEntity<>(newsAdminPageService.getAdminNewsList(pageNum,perPage), HttpStatus.OK);
    }

    @Operation(description = "뉴스 수정 창에서 보여줄 정보 조회")
    @GetMapping("/detail")
    public ResponseEntity<AdminNewsDetailResponse> getAdminNewsDetail(
            @RequestParam() Long newsId
    ) {
        return new ResponseEntity<>(newsAdminPageService.getAdminNewsDetail(newsId), HttpStatus.OK);
    }

    @Operation(description = "뉴스 생성 요청")
    @PostMapping("/create")
    public ResponseEntity adminCreateNews(
            @RequestBody AdminCreateNewsRequestDto adminCreateNewsRequestDto
    ) {
        newsAdminPageService.adminCreateNews(adminCreateNewsRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(description = "뉴스 수정 요청")
    @PostMapping("/modify")
    public ResponseEntity adminModifyNews(
            @RequestBody AdminModifyNewsRequestDto adminModifyNewsRequestDto
    ) {
        newsAdminPageService.adminModifyNews(adminModifyNewsRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(description = "뉴스 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity adminDeleteNews(
        @RequestParam() Long newsId
    ) {
        newsAdminPageService.adminDeleteNews(newsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
