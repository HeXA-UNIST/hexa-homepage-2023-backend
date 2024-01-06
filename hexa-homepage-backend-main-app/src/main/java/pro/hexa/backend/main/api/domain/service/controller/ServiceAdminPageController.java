package pro.hexa.backend.main.api.domain.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminSeminarDetailResponse;
import pro.hexa.backend.main.api.domain.service.dto.AdminCreateServiceRequestDto;
import pro.hexa.backend.main.api.domain.service.dto.AdminModifyServiceRequestDto;
import pro.hexa.backend.main.api.domain.service.dto.AdminServiceDetailResponse;
import pro.hexa.backend.main.api.domain.service.dto.AdminServiceListResponse;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ServiceAdminPageController {

    @Operation(description = "서비스 리스트 조회")
    @GetMapping("/serviceList")
    public ResponseEntity<AdminServiceListResponse> getAdminServiceList() {
        return ResponseEntity.ok(AdminServiceListResponse.builder().build());
    }

    @Operation(description = "서비스 수정 창에서 보여줄 정보 조회")
    @GetMapping("/serviceDetail")
    public ResponseEntity<AdminServiceDetailResponse> getAdminServiceDetail(
        @RequestParam() Long serviceId
    ) {
        return ResponseEntity.ok(AdminServiceDetailResponse.builder().build());
    }

    @Operation(description = "서비스 생성 요청")
    @PostMapping("/createService")
    public ResponseEntity<Void> adminCreateService(@RequestBody AdminCreateServiceRequestDto adminCreateServiceRequestDto) {
        return ResponseEntity.ok(null);
    }

    @Operation(description = "서비스 수정 요청")
    @PostMapping("/modifyService")
    public ResponseEntity<Void> adminModifyService(@RequestBody AdminModifyServiceRequestDto adminModifyServiceRequestDto) {
        return ResponseEntity.ok(null);
    }

    @Operation(description = "서비스 삭제")
    @DeleteMapping("/deleteService")
    public ResponseEntity<Void> adminDeleteService(
        @RequestParam() Long serviceId
    ) {
        return ResponseEntity.ok(null);
    }
}
