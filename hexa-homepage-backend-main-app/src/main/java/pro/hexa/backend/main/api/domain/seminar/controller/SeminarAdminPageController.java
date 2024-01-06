package pro.hexa.backend.main.api.domain.seminar.controller;

import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminCreateSeminarRequestDto;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminModifySeminarRequestDto;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminSeminarDetailResponse;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminSeminarListResponse;
import pro.hexa.backend.main.api.domain.seminar.service.SeminarAdminPageService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class

SeminarAdminPageController {

    private final SeminarAdminPageService seminarAdminPageService;
    @Operation(description = "세미나 리스트 조회")
    @GetMapping("/seminarList")
    public ResponseEntity<AdminSeminarListResponse> getAdminSeminarList(
        @RequestParam(required = false) Integer pageNum,
        @RequestParam(required = false) @Valid @Min(value = 1) Integer perPage
    ) {
        return ResponseEntity.ok(seminarAdminPageService.getAdminSeminarList(pageNum, perPage));
    }

    @Operation(description = "세미나 수정 창에서 보여줄 정보 조회")
    @GetMapping("/seminarDetail")
    public ResponseEntity<AdminSeminarDetailResponse> getAdminSeminarDetail(
        @RequestParam() Long seminarId
    ) {
        return ResponseEntity.ok(seminarAdminPageService.getAdminSeminarDetail(seminarId));
    }

    @Operation(description = "세미나 생성 요청")
    @PostMapping("/createSeminar")
    public ResponseEntity<Void> adminCreateSeminar(@RequestBody AdminCreateSeminarRequestDto adminCreateSeminarRequestDto) {
        seminarAdminPageService.adminCreateSeminar(adminCreateSeminarRequestDto);
        return ResponseEntity.ok(null);
    }

    @Operation(description = "세미나 수정 요청")
    @PostMapping("/modifySeminar")
    public ResponseEntity<Void> adminModifySeminar(@RequestBody AdminModifySeminarRequestDto adminModifySeminarRequestDto) {
        seminarAdminPageService.adminModifySeminar(adminModifySeminarRequestDto);
        return ResponseEntity.ok(null);
    }

    @Operation(description = "세미나 삭제")
    @DeleteMapping("/deleteSeminar")
    public ResponseEntity<Void> adminDeleteSeminar(
        @RequestParam() Long seminarId
    ) {
        seminarAdminPageService.adminDeleteSeminar(seminarId);
        return ResponseEntity.ok(null);
    }


}
